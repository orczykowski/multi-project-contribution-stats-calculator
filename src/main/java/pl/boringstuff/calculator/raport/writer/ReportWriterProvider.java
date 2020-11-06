package pl.boringstuff.calculator.raport.writer;

import pl.boringstuff.calculator.project.ReportFormat;

public class ReportWriterProvider {
  public static ReportWriter provideFor(final ReportFormat format) {
    return switch (format) {
      case HTML -> new HtmlReportWriter();
      case CSV -> new CsvReportWriter();
      case PDF -> new PdfReportWriter();
    };
  }
}
