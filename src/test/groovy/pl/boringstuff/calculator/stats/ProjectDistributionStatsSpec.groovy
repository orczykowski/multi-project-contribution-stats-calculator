package pl.boringstuff.calculator.stats


import static pl.boringstuff.utils.DistributionStatsAssertion.assertDistributionStats
import spock.lang.Specification

class ProjectDistributionStatsSpec extends Specification {

  def "should create project distribution stats"() {
    given:
      def locDist = 1.0
      def commitsDist = 12.91
      def filesDist = 16.0

    when:
      def stats = new ProjectDistributionStats(locDist, commitsDist, filesDist)

    then:
      assertDistributionStats(stats)
              .hasPercentOfLineOfCode(locDist)
              .hasPercentOfCommits(commitsDist)
              .hasPercentOfFiles(filesDist)
  }

  def "should create initial project distribution stats for null values"() {
    when:
      def stats = new ProjectDistributionStats(null, null, null)

    then:
      assertDistributionStats(stats).isEmpty()
  }

  def "should ignore negative values and set participation as 0"() {
    given:
      def negativeValue = new BigDecimal("-1");
    when:
      def stats = new ProjectDistributionStats(negativeValue, negativeValue, negativeValue)

    then:
      assertDistributionStats(stats).isEmpty()
  }

}
