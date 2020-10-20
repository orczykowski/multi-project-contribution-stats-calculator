package pl.boringstuff.calculator.project;

public class FetchingProjectException extends RuntimeException {
  private static final String MESSAGE_PATTERN = "Problem with fetching projects definitions, details [%s]";

  public FetchingProjectException(final String message) {
    super(MESSAGE_PATTERN.formatted(message));
  }
}
