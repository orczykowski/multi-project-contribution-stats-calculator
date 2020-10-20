package pl.boringstuff.calculator.stats


import static pl.boringstuff.utils.ContributionStatsAssertion.assertContributionStats
import spock.lang.Specification

class ContributionStatsSpec extends Specification {

  def "should create contribution stats"() {
    given:
      def loc = 5412L
      def commits = 12L
      def files = 115L

    when:
      def stats = createStat(loc, commits, files)

    then:
      assertContributionStats(stats)
              .hasLineOfCode(loc)
              .hasNumberOfFiles(files)
              .hasNumberOfCommits(commits)
  }

  def "should create initial project distribution stats for null values"() {
    when:
      def stats = createStat(null, null, null)

    then:
      assertContributionStats(stats).isEmpty()
  }

  def "should ignore negative values and set value as 0"() {
    given:
      def negativeValue = -1L
    when:
      def stats = createStat(negativeValue, negativeValue, negativeValue)

    then:
      assertContributionStats(stats).isEmpty()
  }

  def "should create empty stats"() {
    when:
      def stats = ContributionStats.empty()

    then:
      assertContributionStats(stats).isEmpty()
  }

  def "should correctly compare two contribution stats"() {
    when:
      def result = bigger <=> smaller

    then:
      result == expectedResult

    where:
      smaller             | bigger              || expectedResult
      createStat(1)       | createStat(1)       || 0
      createStat(1)       | createStat(2)       || 1
      createStat(1, 1)    | createStat(1, 1)    || 0
      createStat(1, 1)    | createStat(1, 2)    || 1
      createStat(1, 1, 1) | createStat(1, 1, 1) || 0
      createStat(1, 1, 1) | createStat(1, 1, 2) || 1
  }

  def "should return true when stats has any important impact on contribution"() {
    given:
      def stat = createStat(loc, commits, files)

    when:
      def hasAnImpact = stat.isSignificantContribution()

    then:
      hasAnImpact == excpectedResult

    where:
      loc | commits | files || excpectedResult
      1   | 0       | 0     || true
      0   | 0       | 0     || false
      0   | 1       | 0     || false
      0   | 0       | 1     || false
      0   | 1       | 1     || false
  }

  def "should add two contribution stats"() {
    given:
      def first = createStat(15, 15, 23)

    and:
      def second = createStat(8, 3, 11)

    when:
      def result = first.add(second)

    then:
      assertContributionStats(result)
              .hasLineOfCode(23L)
              .hasNumberOfFiles(34L)
              .hasNumberOfCommits(18L)
  }

  private static ContributionStats createStat(Long loc, Long commits = 0, Long files = 0) {
    new ContributionStats(loc, commits, files)
  }
}
