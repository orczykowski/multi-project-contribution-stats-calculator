package pl.orczykowski.core.stats.calculator;

public class ParseResultException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "Problem with parse git fame command csv result, details [%s]";

    public ParseResultException(final String message) {
        super(MESSAGE_PATTERN.formatted(message));
    }
}
