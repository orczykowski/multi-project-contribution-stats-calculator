package io.github.orczykowski.core.stats.model

import io.github.orczykowski.core.project.Project
import io.github.orczykowski.core.project.User
import spock.lang.Specification

import static io.github.orczykowski.testutils.ContributionStatsAssertion.assertContributionStats

class ProjectStatsSpec extends Specification {
    static final HULK = new User("HULK")
    static final BATMAN = new User("BATMAN")

    static final def HULK_STATS = new UserContributionStats(
            HULK,
            new ContributionStats(1, 2, 3),
            new ProjectDistributionStats(4.0, 5.0, 6.0))

    static final def BATMAN_STATS = new UserContributionStats(
            BATMAN,
            new ContributionStats(5, 6, 7),
            new ProjectDistributionStats(1.5, 1.6, 1.7))

    static final AQUAMAN_STATS = new UserContributionStats(new User("AQUAMAN"),
            new ContributionStats(0, 600, 0),
            new ProjectDistributionStats(0.0, 1.6, 0.0))

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
                .hasLineOfCode(6)
                .hasNumberOfCommits(8)
                .hasNumberOfFiles(10)
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
        resultUserStats.find { it.user() == HULK } == HULK_STATS
        resultUserStats.find { it.user() == BATMAN } == BATMAN_STATS
    }
}
