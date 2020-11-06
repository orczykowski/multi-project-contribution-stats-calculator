package pl.boringstuff.infrastructure.config;

import pl.boringstuff.calculator.project.ReportFormat;

import java.nio.file.Paths;
import java.time.LocalDate;

class AppDefaultsConfig {
  private static final String CURRENT_DIR = Paths.get("").toAbsolutePath().toString().concat("/");
  static final LocalDate DEFAULT_DATE_FROM = LocalDate.parse("1970-01-01");
  static final ReportFormat DEFAULT_REPORT_FORMAT = ReportFormat.HTML;
  static final String DEFAULT_WORKING_DIR = CURRENT_DIR.concat("tmp_repos/");
  static final String DEFAULT_RESULT_DIR = CURRENT_DIR.concat("reports/");
  static final String DEFAULT_PROJECT_REPOSITORY_PATH = CURRENT_DIR.concat("projects.json");
}
