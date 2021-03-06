package pl.boringstuff.adapter.writer;

import pl.boringstuff.core.project.ReportFormat;

public class ReportException extends RuntimeException {
  private static final String MESSAGE_PATTERN = "Problem with creating or save %s report, details [%s]";

  public ReportException(final String message, final ReportFormat format) {
    super(MESSAGE_PATTERN.formatted(format.name(), message));
  }
}
