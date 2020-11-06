package pl.boringstuff.calculator

import pl.boringstuff.calculator.project.Project
import pl.boringstuff.infrastructure.config.CalculationParamsProvider
import spock.lang.Specification

import java.time.LocalDate

class CommandFactorySpec extends Specification {

  static final String URL = "git@github.com:orczykowski/multi-project-contribution-stats-calculator.git"
  static final Project PROJECT = new Project(URL, null)

  def setup(){
    CalculationParamsProvider.init([] as String[])
  }

  def "should create command to checkout repo"() {
    when:
      def result = CommandFactory.cloneRepoCommand(PROJECT)

    then:
      result.command() == "git clone $URL"
  }

  def "should create result to calculate stats form pass begin calculation date"() {
    given:
      def beginDate = LocalDate.parse("2020-10-10")
    when:
      def result = CommandFactory.calculateContributionStatsCommand(PROJECT, beginDate)

    then:
      result.command() == "git fame --hide-progressbar --format=csv --after=2020-10-10"
  }

  def "should create result with excluded paths"() {
    given:
      def project = new Project(URL, ["/some_path", "/another_path"] as Set)

    when:
      def result = CommandFactory.calculateContributionStatsCommand(project, LocalDate.parse("2020-10-10"))

    then:
      result.command() == "git fame --hide-progressbar --format=csv --after=2020-10-10 --exclude=/some_path,/another_path"
  }

  def "should create result to remove repo when it is not needed"() {
    when:
      def result = CommandFactory.removeRepo(PROJECT)

    then:
      result.command().contains("rm -rf")
      result.command().contains("/tmp_repos/multi-project-contribution-stats-calculator")
  }
}
