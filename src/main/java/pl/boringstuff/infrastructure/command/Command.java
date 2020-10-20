package pl.boringstuff.infrastructure.command;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNullElse;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Command {
  private final String command;
  private final File directory;
  private final Set<String> args;

  private Command(final String command, final Set<String> args, final File directory) {
    check(command);
    this.command = command.trim();
    this.args = unmodifiableSet(requireNonNullElse(args, emptySet()));
    this.directory = directory;
  }

  String command() {
    return command;
  }

  String[] args() {
    return args.toArray(new String[args.size()]);
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

  private void check(final String command) {
    if (Objects.isNull(command) || command.isBlank()) {
      throw new IllegalStateException();
    }
  }

  public static class Builder {
    private String command;
    private File directory;
    private String flags = "";

    private Builder(final String command) {
      this.command = command;
    }

    public Builder inDictionary(final String dictionary) {
      this.directory = new File(dictionary);
      return this;
    }

    public Builder withArgs(final List<String> flags) {
      this.flags = String.join(" ", flags);
      return this;
    }

    public Command build() {
      return new Command(command.trim() + " " + flags, null, directory);
    }
  }
}