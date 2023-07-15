package io.github.orczykowski.core;

import java.io.IOException;
import java.nio.file.Path;

public class FetchingProjectException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "cannot fetch projects from repository [%s], error = [%s]";

    public FetchingProjectException(final Path repoPath, final IOException e) {
        super(MESSAGE_PATTERN.formatted(repoPath, e.getMessage()), e.getCause());
    }
}
