package pl.boringstuff.core.raport;

import pl.boringstuff.core.project.ReportFormat;

public interface ReportWriter {

    void write(final ContributionReport report);

    ReportFormat format();
}
