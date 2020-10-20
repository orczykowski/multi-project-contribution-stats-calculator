package pl.boringstuff.utils

import pl.boringstuff.calculator.project.ReportFormat
import static pl.boringstuff.infrastructure.APP_DEFAULTS.DATE_FROM
import static pl.boringstuff.infrastructure.APP_DEFAULTS.REPORT_FORMAT
import static pl.boringstuff.infrastructure.APP_DEFAULTS.REPO_PATH
import static pl.boringstuff.infrastructure.APP_DEFAULTS.RESULT_DIR
import pl.boringstuff.infrastructure.CalculationParameters

import java.time.LocalDate

class CalculationParamsAssertion {
  private CalculationParameters subject

  private CalculationParamsAssertion(final CalculationParameters subject) {
    this.subject = subject
  }

  static CalculationParamsAssertion assertCalculationParams(final CalculationParameters params) {
    assert params != null
    new CalculationParamsAssertion(params)
  }

  def hasDateFrom(final LocalDate dateFrom) {
    assert this.subject.dateFrom() == dateFrom
    this
  }

  def hasRepoPath(final String repoPath) {
    assert this.subject.repoPath() == repoPath
    this
  }

  def hasResultDir(final String resultDir) {
    assert this.subject.resultDir() == resultDir
    this
  }

  def hasReportFormat(final ReportFormat format) {
    assert this.subject.reportFormat() == format
    this
  }

  def hasDefaultDateFrom() {
    hasDateFrom(LocalDate.parse(DATE_FROM.getValue()))
  }

  def hasDefaultRepoPath() {
    hasRepoPath(REPO_PATH.getValue())
  }

  def hasDefaultResultDir() {
    hasResultDir(RESULT_DIR.getValue())
  }

  def hasDefaultReportFormat() {
    hasReportFormat(ReportFormat.valueOf(REPORT_FORMAT.getValue()))
  }

}
