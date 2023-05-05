package pl.orczykowski.infrastructure.command;

@FunctionalInterface
public interface ExecutableCommand {

    CommandExecutionResult execute();
}
