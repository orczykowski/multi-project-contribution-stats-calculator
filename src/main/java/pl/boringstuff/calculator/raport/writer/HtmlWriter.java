package pl.boringstuff.calculator.raport.writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import static pl.boringstuff.calculator.project.ReportFormat.HTML;
import pl.boringstuff.calculator.raport.ContributionReport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlWriter implements ReportWriter {
  private static final String TEMPLATE_NAME = "reportTemplate.ftl";
  private static final String REPORTS_DIR = "reports";

  @Override
  public void write(final ContributionReport report) {
    try (var fileWriter = new FileWriter(createReportFile(report))) {
      var template = getReportTemplate();
      template.process(asMap(report), fileWriter);
    } catch (IOException | TemplateException ex) {
      throw new ReportException(ex.getMessage(), HTML);
    }
  }

  private Map<String, Object> asMap(final ContributionReport report) {
    return Map.of("report", report);
  }

  private File createReportFile(final ContributionReport report) {
    return new File("%s/contribution-report-%s.html".formatted(REPORTS_DIR, report.calculationDate()));
  }

  private Template getReportTemplate() throws IOException {
    var configuration = configureTemplateEngine();
    return configuration.getTemplate(TEMPLATE_NAME);
  }

  private Configuration configureTemplateEngine() {
    var cfg = new Configuration(Configuration.VERSION_2_3_30);
    cfg.setClassForTemplateLoading(HtmlWriter.class, "/");
    cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
    cfg.setFallbackOnNullLoopVariable(false);
    return cfg;
  }
}
