package io.github.orczykowski.core.raport;

import io.github.orczykowski.core.project.ReportFormat;

public interface ReportWriter {

    void write(final ContributionReport report);

    ReportFormat format();
}
