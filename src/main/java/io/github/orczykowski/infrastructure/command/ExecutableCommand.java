package io.github.orczykowski.infrastructure.command;

@FunctionalInterface
public interface ExecutableCommand {

    CommandExecutionResult execute();
}
