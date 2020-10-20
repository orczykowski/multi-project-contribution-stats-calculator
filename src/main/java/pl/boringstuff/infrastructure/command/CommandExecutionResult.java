package pl.boringstuff.infrastructure.command;


public sealed interface CommandExecutionResult permits CommandExecutionResult.Failure, CommandExecutionResult.Success {
  final class Failure implements CommandExecutionResult {
    private final String command;
    private final String error;

    public Failure(final String command, final String error) {
      this.command = command;
      this.error = error;
    }

    public String errorMessage() {
      return "Problem with execute command [%s], message[%s]".formatted(command, error);
    }
  }

  record Success(String stringResult) implements CommandExecutionResult {
  }
}
