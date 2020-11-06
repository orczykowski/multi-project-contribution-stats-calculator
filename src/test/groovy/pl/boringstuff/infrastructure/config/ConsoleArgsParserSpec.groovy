package pl.boringstuff.infrastructure.config

import static pl.boringstuff.calculator.project.ReportFormat.CSV
import static pl.boringstuff.utils.CalculationParamsAssertion.assertCalculationParams
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class ConsoleArgsParserSpec extends Specification {

  @Subject
  ConsoleArgsParser argsParser = ConsoleArgsParser.getInstance()

  def "should create calculation params with defaults when no arg is pass from command line"() {
    given:
      def args = new String[0]

    when:
      def calculationParams = argsParser.parse(args)

    then:
      assertCalculationParams(calculationParams)
              .hasDefaultDateFrom()
              .hasDefaultRepoPath()
              .hasDefaultReportFormat()
              .hasDefaultResultDir()
              .hasDefaultWorkingDir()
  }

  def "should create calculation params from console params"() {
    given:
      def dateFrom = "2020-10-10"
      def path = "highway_to_hell"
      def format = CSV
      def resultDir = "result_dir"
      def workingDir = "result_dir"

    and:
      def args = ["dateFrom=$dateFrom",
                  "resultDir=$resultDir",
                  "repoPath=$path",
                  "reportFormat=${format.name()}",
                  "workingDir=$workingDir"] as String[]

    when:
      def calculationParams = argsParser.parse(args)

    then:
      assertCalculationParams(calculationParams)
              .hasDateFrom(LocalDate.parse(dateFrom))
              .hasRepoPath(path)
              .hasReportFormat(format)
              .hasResultDir(resultDir)
              .hasWorkingDir(workingDir)
  }

  def "should ignore case of report format"() {
    given:
      def args = new String[]{"reportFormat=$format"}

    when:
      def calculationParams = argsParser.parse(args)

    then:
      assertCalculationParams(calculationParams)
              .hasReportFormat(CSV)
    where:
      format << ["cSv", "CsV", "CSV", "csv"]

  }

  def "should tim whitespaces form param values"() {
    given:
      def args = new String[]{"reportFormat=CSV "}

    when:
      def calculationParams = argsParser.parse(args)

    then:
      assertCalculationParams(calculationParams)
              .hasReportFormat(CSV)
  }

  def "should ignore unexpected params"() {
    when:
      def calculationParams = argsParser.parse([args] as String[])

    then:
      assertCalculationParams(calculationParams)
              .hasDefaultResultDir()
              .hasDefaultRepoPath()
              .hasDefaultReportFormat()
              .hasDefaultResultDir()
    where:
      args << ["someParam=HULK", "result=asd", "defaultResult=asd"]
  }
}
