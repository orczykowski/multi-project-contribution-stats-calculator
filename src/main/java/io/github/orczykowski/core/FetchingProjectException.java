package io.github.orczykowski.core;

import java.nio.file.Path;

public class FetchingProjectException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "cannot fetch projects from repository [%s], error = [%s]";

    public FetchingProjectException(final Path repoPath, final Exception e) {
        super(MESSAGE_PATTERN.formatted(repoPath, e.getMessage()), e.getCause());
    }
}
