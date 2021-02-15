package pl.boringstuff.infrastructure.command;

@FunctionalInterface
public interface ExecutableCommand {
  CommandExecutionResult execute();
}
