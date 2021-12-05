package pl.boringstuff.infrastructure.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

class SystemCommandExecutor {

  private static final Logger log = LoggerFactory.getLogger(SystemCommandExecutor.class);
  private static final String EMPTY = "";

  SystemCommandExecutor() {
  }

  CommandExecutionResult execute(final ExecutableSystemCommand command) {
    Process process = null;
    try {
      log.debug("running command {}", command);
      process = Runtime.getRuntime().exec(command.command(), command.envp(), command.directory());
      process.waitFor();
      var maybeFailure = readErrorsIfOccurred(process);
      if (maybeFailure.isPresent()) {
        return new CommandExecutionResult.Failure(command.command(), maybeFailure.get());
      }
      return new CommandExecutionResult.Success(asStringResult(process));
    } catch (IOException | InterruptedException ex) {
      log.error("Error: {}", ex.getMessage(), ex);
      return new CommandExecutionResult.Failure(command.command(), ex.getMessage());
    } finally {
      safeDestroy(process);
    }
  }

  private String asStringResult(final Process process) throws IOException {
    try (final var reader = new BufferedReader(
            new InputStreamReader(process.getInputStream(), UTF_8))) {
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
