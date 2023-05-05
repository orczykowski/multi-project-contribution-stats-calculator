package pl.orczykowski.core.raport;

import pl.orczykowski.core.project.ReportFormat;

public interface ReportWriter {

    void write(final ContributionReport report);

    ReportFormat format();
}
