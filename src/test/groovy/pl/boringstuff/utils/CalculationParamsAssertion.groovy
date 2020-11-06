package pl.boringstuff.utils

import pl.boringstuff.calculator.project.ReportFormat
import pl.boringstuff.infrastructure.config.AppDefaultsConfig
import pl.boringstuff.infrastructure.config.CalculationParameters

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
    assert this.subject.repoPath().endsWith(repoPath)
    this
  }

  def hasResultDir(final String resultDir) {
    assert this.subject.resultDir().endsWith(resultDir)
    this
  }

  def hasWorkingDir(final String workingDir) {
    assert this.subject.workingDir().endsWith(workingDir)
    this
  }

  def hasReportFormat(final ReportFormat format) {
    assert this.subject.reportFormat() == format
    this
  }

  def hasDefaultDateFrom() {
    hasDateFrom(AppDefaultsConfig.DEFAULT_DATE_FROM)
  }

  def hasDefaultRepoPath() {
    hasRepoPath(AppDefaultsConfig.DEFAULT_PROJECT_REPOSITORY_PATH)
  }

  def hasDefaultWorkingDir() {
    hasWorkingDir(AppDefaultsConfig.DEFAULT_WORKING_DIR)
  }

  def hasDefaultResultDir() {
    hasResultDir(AppDefaultsConfig.DEFAULT_RESULT_DIR)
  }

  def hasDefaultReportFormat() {
    hasReportFormat(AppDefaultsConfig.DEFAULT_REPORT_FORMAT)
  }

}
