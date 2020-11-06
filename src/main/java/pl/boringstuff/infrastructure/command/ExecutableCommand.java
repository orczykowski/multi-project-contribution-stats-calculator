package pl.boringstuff.infrastructure.command;

import pl.boringstuff.infrastructure.utils.Preconditions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ExecutableCommand {
  private final String command;
  private final File directory;

  private ExecutableCommand(final String command, final List<String> args, final File directory) {
    check(command);
    this.command = withArgs(command, args);
    this.directory = directory;
  }

  String command() {
    return command;
  }

  String[] envp() {
    return new String[0];
  }

  File directory() {
    return directory;
  }

  public static Builder newCommand(final String command) {
    return new Builder(command);
  }

  public CommandExecutionResult execute() {

    return new CommandExecutor().execute(this);
  }

  private String withArgs(String command, List<String> args) {
    final var stringArgs = args.stream()
            .filter(Objects::nonNull)
            .filter(arg -> !arg.isBlank())
            .collect(Collectors.joining(" "));
    return "%s %s".formatted(command, stringArgs).trim();
  }

  private void check(final String command) {
    Preconditions.checkState(Objects::nonNull, command);
    Preconditions.checkState((arg) -> !arg.isBlank(), command);
  }

  public static class Builder {
    private final String command;
    private File directory;
    private List<String> args = new ArrayList<>();

    private Builder(final String command) {
      this.command = command;
    }

    public Builder inDictionary(final String dictionary) {
      this.directory = new File(dictionary);
      return this;
    }

    public Builder withArgs(final List<String> args) {
      this.args = args;
      return this;
    }

    public ExecutableCommand build() {
      return new ExecutableCommand(command, args, directory);
    }
  }
}