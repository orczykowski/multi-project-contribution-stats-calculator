package pl.boringstuff.calculator.project

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class ProjectSpec extends Specification {

  static final String URL = "git@github.com:orczykowski/multi-project-contribution-stats-calculator.git"

  def "should create full project from url and excluded paths"() {
    given:
      def excludedPaths = ["simple", "path"] as Set
    when:
      def project = new Project(URL, excludedPaths)

    then:
      project.repositoryUrl == URL
      project.excludePaths == excludedPaths
      project.name == "multi-project-contribution-stats-calculator"
  }

  def "should throw exception when repo url is blank"() {
    when:
      new Project(url, [] as Set)

    then:
      thrown(IllegalArgumentException)

    where:
      url << ["", " ", null]
  }

  def "should create project form correct url with empty excluded path when its not pass"() {
    when:
      def result = new Project(URL, exculdedPaths)

    then:
      result.excludePaths == [] as Set

    where:
      exculdedPaths << [null, new HashSet<>()]
  }

  def "should create extract and normalize project name form url"() {
    when:
      def result = new Project(url, [] as Set)

    then:
      result.name == "multi-project-contribution-stats-calculator"

    where:
      url << [
              "git@github.com:orczykowski/multi-project-contribution-stats-calculator.git",
              "GIT@GITHUB.COM:ORCZYKOWSKI/MULTI-PROJECT-CONTRIBUTION-STATS-CALCULATOR.GIT"
      ]
  }

  def "should join all excluded paths to string by pass delimiter"() {
    given:
      def project = new Project(URL, ["first", "second"] as Set)

    when:
      def result = project.getJoinedExcludedPaths(",")

    then:
      result == "first,second"
  }

  def "should return empty string when project does not have nay excluded path"() {
    given:
      def project = new Project(URL, [] as Set)

    when:
      def result = project.getJoinedExcludedPaths(",")

    then:
      result == ""
  }

  def "hashCode equals contract is fulfilled"() {
    expect:
      EqualsVerifier.forClass(Project).usingGetClass().verify()
  }
}
