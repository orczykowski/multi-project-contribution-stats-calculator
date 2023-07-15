package io.github.orczykowski.core.stats.model

import spock.lang.Specification

import static io.github.orczykowski.testutils.ContributionStatsAssertion.assertContributionStats

class ContributionStatsSpec extends Specification {

    def "should create contribution stats"() {
        given:
        def commits = 12L
        def linesAdded = 5412L
        def linesRemoved = 115L

        when:
        def stats = new ContributionStats(commits, linesAdded, linesRemoved)

        then:
        assertContributionStats(stats)
                .hasCommits(commits)
                .hasLinesAdded(linesAdded)
                .hasLinesRemoved(linesRemoved)
    }

    def "should create initial contribution stats for null values"() {
        when:
        def stats = new ContributionStats(null, null, null)

        then:
        assertContributionStats(stats).isEmpty()
    }

    def "should ignore negative values and set value as 0"() {
        given:
        def negativeValue = -1L
        when:
        def stats = new ContributionStats(negativeValue, negativeValue, negativeValue)

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
        smaller                | bigger                 || expectedResult
        createStat(1)          | createStat(1)          || 0
        createStat(1, 1)       | createStat(1, 2)       || 1
        createStat(1, 1)       | createStat(1, 1)       || 0
        createStat(1, 1, 1)    | createStat(1, 1, 1)    || 0
        createStat(1, 1, 1)    | createStat(1, 1, 2)    || 1
        createStat(1, 1, 1)    | createStat(2, 1, 1)    || 1
    }

    def "should return true when stats has any important impact on contribution"() {
        given:
        def stat = createStat(commits, linesAdded, linesRemoved)

        when:
        def hasAnImpact = stat.isSignificantContribution()

        then:
        hasAnImpact == expectedResult

        where:
        commits | linesAdded | linesRemoved || expectedResult
        0       | 1          | 0            || true
        0       | 0          | 1            || true
        0       | 1          | 1            || true
        1       | 0          | 0            || false
        0       | 0          | 0            || false
    }

    def "should add two contribution stats"() {
        given:
        def first = new ContributionStats(15L, 15L, 23L, 5L, 2L, 10L, 3L, 2L)

        and:
        def second = new ContributionStats(3L, 8L, 11L, 3L, 1L, 4L, 2L, 2L)

        when:
        def result = first.add(second)

        then:
        assertContributionStats(result)
                .hasCommits(18L)
                .hasLinesAdded(23L)
                .hasLinesRemoved(34L)
                .hasFilesChanged(8L)
                .hasNewFiles(3L)
                .hasProductionLinesAdded(14L)
                .hasTestLinesAdded(5L)
                .hasConfigLinesAdded(4L)
    }

    def "should create full contribution stats with all 8 fields"() {
        when:
        def stats = new ContributionStats(10L, 200L, 50L, 15L, 5L, 150L, 30L, 20L)

        then:
        assertContributionStats(stats)
                .hasCommits(10L)
                .hasLinesAdded(200L)
                .hasLinesRemoved(50L)
                .hasFilesChanged(15L)
                .hasNewFiles(5L)
                .hasProductionLinesAdded(150L)
                .hasTestLinesAdded(30L)
                .hasConfigLinesAdded(20L)
    }

    def "should normalize all 8 fields for null values"() {
        when:
        def stats = new ContributionStats(null, null, null, null, null, null, null, null)

        then:
        assertContributionStats(stats).isEmpty()
    }

    def "should normalize all 8 fields for negative values"() {
        when:
        def stats = new ContributionStats(-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L)

        then:
        assertContributionStats(stats).isEmpty()
    }

    def "should use convenience 3-arg constructor with defaults for new fields"() {
        when:
        def stats = new ContributionStats(5L, 10L, 3L)

        then:
        assertContributionStats(stats)
                .hasCommits(5L)
                .hasLinesAdded(10L)
                .hasLinesRemoved(3L)
                .hasFilesChanged(0L)
                .hasNewFiles(0L)
                .hasProductionLinesAdded(0L)
                .hasTestLinesAdded(0L)
                .hasConfigLinesAdded(0L)
    }

    private static ContributionStats createStat(Long commits, Long linesAdded = 0, Long linesRemoved = 0) {
        new ContributionStats(commits, linesAdded, linesRemoved)
    }
}
