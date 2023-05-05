package pl.orczykowski.infrastructure.command;


public sealed interface CommandExecutionResult permits CommandExecutionResult.Failure,
        CommandExecutionResult.Success {

    String result();

    final class Failure implements CommandExecutionResult {

        private final String command;
        private final String error;

        public Failure(final String command, final String error) {
            this.command = command;
            this.error = error;
        }

        @Override
        public String result() {
            return "Problem with execute command [%s], message[%s]".formatted(command, error);
        }
    }

    record Success(String stringResult) implements CommandExecutionResult {

        @Override
        public String result() {
            return stringResult;
        }
    }
}
