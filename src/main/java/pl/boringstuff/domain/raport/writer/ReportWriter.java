package pl.boringstuff.domain.raport.writer;

import pl.boringstuff.domain.project.ReportFormat;
import pl.boringstuff.domain.raport.ContributionReport;

public interface ReportWriter {
  void write(final ContributionReport report);

  ReportFormat format();
}
