package pl.boringstuff.infrastructure.command;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

class CommandExecutor {
  private static final String EMPTY = "";

  CommandExecutor() {
  }

  CommandExecutionResult execute(final ExecutableCommand command) {
    Process process = null;
    try {
      process = Runtime.getRuntime().exec(command.command(), command.envp(), command.directory());
      process.waitFor();
      var maybeFailure = readErrorsIfOccurred(process);
      if (maybeFailure.isPresent()) {
        return new CommandExecutionResult.Failure(command.command(), maybeFailure.get());
      }
      return new CommandExecutionResult.Success(asStringResult(process));
    } catch (IOException | InterruptedException ex) {
      return new CommandExecutionResult.Failure(command.command(), ex.getMessage());
    } finally {
      safeDestroy(process);
    }
  }

  private String asStringResult(final Process process) throws IOException {
    try (final var reader = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
      return reader.lines()
              .map(it -> it + "\n")
              .reduce(String::concat)
              .orElse(EMPTY);
    }
  }

  private void safeDestroy(final Process process) {
    if (process != null) {
      process.destroy();
    }
  }

  private Optional<String> readErrorsIfOccurred(final Process process) throws IOException {
    if (process.exitValue() == 0) {
      return Optional.empty();
    }
    return readStream(process.getErrorStream());
  }

  private Optional<String> readStream(final InputStream stream) throws IOException {
    try (final var reader = new BufferedReader(new InputStreamReader(stream))) {
      return reader.lines()
              .reduce(String::concat);
    }
  }
}
