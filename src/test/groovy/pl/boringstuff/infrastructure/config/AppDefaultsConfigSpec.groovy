package pl.boringstuff.infrastructure.config

import pl.boringstuff.calculator.project.ReportFormat
import spock.lang.Specification

import java.time.LocalDate

class AppDefaultsConfigSpec extends Specification {
  def "should have information about all default values for calculation params"() {
    expect:
      AppDefaultsConfig.DEFAULT_PROJECT_REPOSITORY_PATH.endsWith("/multi-project-contribution-stats-calculator/projects.json")
      AppDefaultsConfig.DEFAULT_WORKING_DIR.endsWith("/multi-project-contribution-stats-calculator/tmp_repos/")
      AppDefaultsConfig.DEFAULT_REPORT_FORMAT == ReportFormat.HTML
      AppDefaultsConfig.DEFAULT_DATE_FROM == LocalDate.parse("1970-01-01")
      AppDefaultsConfig.DEFAULT_RESULT_DIR.endsWith("/multi-project-contribution-stats-calculator/reports/")
  }
}
