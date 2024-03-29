package io.github.orczykowski.adapter.writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.orczykowski.core.CalculationSpecificationSupplier;
import org.springframework.stereotype.Component;
import io.github.orczykowski.core.project.ReportFormat;
import io.github.orczykowski.core.raport.ContributionReport;
import io.github.orczykowski.core.raport.ReportWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static io.github.orczykowski.core.project.ReportFormat.HTML;

@Component
public class HtmlReportWriter implements ReportWriter {

  private static final String TEMPLATE_NAME = "reportTemplate.ftl";

  private final CalculationSpecificationSupplier specificationSupplier;

  HtmlReportWriter(final CalculationSpecificationSupplier specificationSupplier) {
    this.specificationSupplier = specificationSupplier;
  }

  @Override
  public void write(final ContributionReport report) {
    try (final var writer = Files.newBufferedWriter(createReportPath(report))) {
      final var template = getReportTemplate();
      template.process(asMap(report), writer);
    } catch (IOException | TemplateException ex) {
      throw new ReportException(ex.getMessage(), HTML);
    }
  }

  @Override
  public ReportFormat format() {
    return HTML;
  }

  private Path createReportPath(final ContributionReport report) {
    return specificationSupplier.supply().reportPath(report.calculationDate());
  }

  private Map<String, Object> asMap(final ContributionReport report) {
    return Map.of("report", report);
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
