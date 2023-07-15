package io.github.orczykowski.core.stats.model

import spock.lang.Specification

import static io.github.orczykowski.testutils.DistributionStatsAssertion.assertDistributionStats

class ProjectDistributionStatsSpec extends Specification {

    def "should create project distribution stats"() {
        given:
        def commitsDist = 12.91
        def linesAddedDist = 1.0
        def linesRemovedDist = 16.0

        when:
        def stats = new ProjectDistributionStats(commitsDist, linesAddedDist, linesRemovedDist)

        then:
        assertDistributionStats(stats)
                .hasPercentOfCommits(commitsDist)
                .hasPercentOfLinesAdded(linesAddedDist)
                .hasPercentOfLinesRemoved(linesRemovedDist)
    }

    def "should create initial project distribution stats for null values"() {
        when:
        def stats = new ProjectDistributionStats(null, null, null)

        then:
        assertDistributionStats(stats).isEmpty()
    }

    def "should ignore negative values and set participation as 0"() {
        given:
        def negativeValue = new BigDecimal("-1")
        when:
        def stats = new ProjectDistributionStats(negativeValue, negativeValue, negativeValue)

        then:
        assertDistributionStats(stats).isEmpty()
    }

}
