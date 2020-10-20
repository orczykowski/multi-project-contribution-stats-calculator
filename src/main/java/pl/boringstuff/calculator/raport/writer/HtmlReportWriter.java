package pl.boringstuff.calculator.raport.writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import static pl.boringstuff.calculator.project.ReportFormat.HTML;
import pl.boringstuff.calculator.raport.ContributionReport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class HtmlReportWriter implements ReportWriter {
  private static final String TEMPLATE_NAME = "reportTemplate.ftl";
  private static final String REPORTS_DIR = "reports";

  @Override
  public void write(final ContributionReport report) {
    try (var writer = Files.newBufferedWriter(createReportFile(report))) {
      var template = getReportTemplate();
      template.process(asMap(report), writer);
    } catch (IOException | TemplateException ex) {
      throw new ReportException(ex.getMessage(), HTML);
    }
  }

  private Map<String, Object> asMap(final ContributionReport report) {
    return Map.of("report", report);
  }

  private Path createReportFile(final ContributionReport report) {
    return Path.of("%s/contribution-report-%s.html".formatted(REPORTS_DIR, report.calculationDate()));
  }

  private Template getReportTemplate() throws IOException {
    var configuration = configureTemplateEngine();
    return configuration.getTemplate(TEMPLATE_NAME);
  }

  private Configuration configureTemplateEngine() {
    var cfg = new Configuration(Configuration.VERSION_2_3_30);
    cfg.setClassForTemplateLoading(HtmlReportWriter.class, "/");
    cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
    cfg.setFallbackOnNullLoopVariable(false);
    return cfg;
  }
}
