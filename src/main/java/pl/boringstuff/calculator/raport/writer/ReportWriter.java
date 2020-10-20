package pl.boringstuff.calculator.raport.writer;

import freemarker.template.TemplateException;
import pl.boringstuff.calculator.raport.ContributionReport;

import java.io.IOException;

public interface ReportWriter {
  void write(final ContributionReport report) throws IOException, TemplateException;
}
