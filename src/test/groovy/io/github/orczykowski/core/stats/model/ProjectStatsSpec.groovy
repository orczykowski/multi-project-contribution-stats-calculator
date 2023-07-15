package io.github.orczykowski.core.stats.model

import io.github.orczykowski.core.project.Project
import io.github.orczykowski.core.project.User
import spock.lang.Specification

import static io.github.orczykowski.testutils.ContributionStatsAssertion.assertContributionStats
import static io.github.orczykowski.testutils.DistributionStatsAssertion.assertDistributionStats

class ProjectStatsSpec extends Specification {
    static final HULK = new User("HULK")
    static final BATMAN = new User("BATMAN")

    static final def HULK_STATS = new UserContributionStats(
            HULK,
            new ContributionStats(2L, 1L, 3L),
            ProjectDistributionStats.empty())

    static final def BATMAN_STATS = new UserContributionStats(
            BATMAN,
            new ContributionStats(6L, 5L, 7L),
            ProjectDistributionStats.empty())

    static final AQUAMAN_STATS = new UserContributionStats(new User("AQUAMAN"),
            new ContributionStats(600L, 0L, 0L),
            ProjectDistributionStats.empty())

    private Project PROJECT = new Project("git@github.com:orczykowski/multi-project-contribution-stats-calculator.git", [] as Set)

    def "should throw exception when try create project stats without project"() {
        when:
        new ProjectStats(null, null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should set user and total stats to null when user stats is not pass"() {
        when:
        def projectStats = new ProjectStats(PROJECT, null)

        then:
        projectStats.userStats().isEmpty()

        and:
        assertContributionStats(projectStats.total()).isEmpty()
    }

    def "should sum all user significant stats in total contribution stats"() {
        given:
        def userStats = [HULK_STATS, BATMAN_STATS, AQUAMAN_STATS]

        when:
        def projectStats = new ProjectStats(PROJECT, userStats)

        then:
        assertContributionStats(projectStats.total())
                .hasCommits(8)
                .hasLinesAdded(6)
                .hasLinesRemoved(10)
    }

    def "should contains only significant stats for all users"() {
        given:
        def userStats = [HULK_STATS, BATMAN_STATS, AQUAMAN_STATS]
        when:
        def projectStats = new ProjectStats(PROJECT, userStats)

        then:
        def resultUserStats = projectStats.userStats()
        resultUserStats.size() == 2

        and:
        resultUserStats.find { it.user() == HULK } != null
        resultUserStats.find { it.user() == BATMAN } != null
    }

    def "should compute distribution percentages for users"() {
        given:
        def hulkStats = new UserContributionStats(HULK,
                new ContributionStats(3L, 300L, 100L),
                ProjectDistributionStats.empty())
        def batmanStats = new UserContributionStats(BATMAN,
                new ContributionStats(7L, 700L, 400L),
                ProjectDistributionStats.empty())

        when:
        def projectStats = new ProjectStats(PROJECT, [hulkStats, batmanStats])

        then:
        def hulk = projectStats.userStats().find { it.user() == HULK }
        assertDistributionStats(hulk.distribution())
                .hasPercentOfCommits(30.0)
                .hasPercentOfLinesAdded(30.0)
                .hasPercentOfLinesRemoved(20.0)

        and:
        def batman = projectStats.userStats().find { it.user() == BATMAN }
        assertDistributionStats(batman.distribution())
                .hasPercentOfCommits(70.0)
                .hasPercentOfLinesAdded(70.0)
                .hasPercentOfLinesRemoved(80.0)
    }
}
