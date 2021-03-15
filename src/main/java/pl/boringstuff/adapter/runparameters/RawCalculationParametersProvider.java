package pl.boringstuff.adapter.runparameters;

import static java.util.Objects.requireNonNullElse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.CalculationSpecificationSupplier;
import pl.boringstuff.core.project.ReportFormat;

import java.nio.file.FileSystems;
import java.time.LocalDate;

@Component
public class RawCalculationParametersProvider implements CalculationSpecificationSupplier {
  private static final String CURRENT_DIR;
  private static final ReportFormat DEFAULT_REPORT_FORMAT;
  private static final LocalDate DEFAULT_BEGIN_DATE;
  private static final String DEFAULT_RESULT_DIR;
  private static final String DEFAULT_REPO_PATH;
  private static final String DEFAULT_WORKING_DIR;
  private static final Long DEFAULT_TIMEOUT_IN_SECONDS;

  static {
    CURRENT_DIR = FileSystems.getDefault()
            .getPath("")
            .toAbsolutePath()
            .toString();
    DEFAULT_REPORT_FORMAT = ReportFormat.HTML;
    DEFAULT_BEGIN_DATE = LocalDate.parse("1970-01-01");
    DEFAULT_RESULT_DIR = withBaseDir("reports/");
    DEFAULT_REPO_PATH = withBaseDir("projects.json");
    DEFAULT_WORKING_DIR = "/tmp/multi-project-contributions-stats-calculator-working-dir/";
    DEFAULT_TIMEOUT_IN_SECONDS = 60 * 60L;
  }

  private static final Logger log = LoggerFactory.getLogger(RawCalculationParametersProvider.class);

  private final CalculationSpecification specification;

  public RawCalculationParametersProvider(final CalculationParameters parameters) {
    this.specification = new CalculationSpecification(
            getBeginDate(parameters.dateFrom()),
            getPathOrDefault(parameters.resultDir(), DEFAULT_RESULT_DIR),
            getPathOrDefault(parameters.repoPath(), DEFAULT_REPO_PATH),
            getReportFormat(parameters.reportFormat()),
            getPathOrDefault(parameters.workingDir(), DEFAULT_WORKING_DIR),
            requireNonNullElse(parameters.timoutInSeconds(), DEFAULT_TIMEOUT_IN_SECONDS));
  }

  @Override
  public CalculationSpecification supply() {
    return specification;
  }

  private LocalDate getBeginDate(final String dateFrom) {
    if (Strings.isBlank(dateFrom)) {
      return DEFAULT_BEGIN_DATE;
    }
    return LocalDate.parse(dateFrom);
  }

  private String getPathOrDefault(final String path, final String defaultPath) {
    if (Strings.isBlank(path)) {
      return defaultPath;
    }
    return path;
  }

  private static String withBaseDir(final String path) {
    return "%s/%s".formatted(CURRENT_DIR, path);
  }

  private ReportFormat getReportFormat(final String format) {
    if (Strings.isBlank(format)) {
      return DEFAULT_REPORT_FORMAT;
    }
    try {
      return ReportFormat.valueOf(format);
    } catch (Exception e) {
      log.error("Invalid report format {}, allows formats [{}]", format, ReportFormat.values());
      return DEFAULT_REPORT_FORMAT;
    }
  }

}
