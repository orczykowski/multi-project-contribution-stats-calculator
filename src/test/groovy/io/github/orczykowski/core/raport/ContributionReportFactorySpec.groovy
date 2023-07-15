package io.github.orczykowski.core.raport


import io.github.orczykowski.core.project.Project
import io.github.orczykowski.core.project.User
import io.github.orczykowski.core.stats.model.ContributionStats
import io.github.orczykowski.core.stats.model.ProjectDistributionStats
import io.github.orczykowski.core.stats.model.ProjectStats
import io.github.orczykowski.core.stats.model.UserContributionStats
import io.github.orczykowski.testutils.ContributionReportAssertion
import spock.lang.Specification
import spock.lang.Subject

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class ContributionReportFactorySpec extends Specification {
    private static final def HULK = "hulk"
    private static final def BATMAN = "batman"

    private static final def DATE_FROM = LocalDate.parse("2020-01-01")
    private static final Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

    @Subject
    ContributionReportFactory reportFactory = new ContributionReportFactory(clock)

    def "should create report for calculation result"() {
        given:
            def avengersProjectName = "avengers"

            def avengerContributionStats = [contributionStats(HULK, 15L, 5000L, 20L),
                                            contributionStats(BATMAN, 1L, 0L, 0L)]
            def avengersStats = createCalculationResult(avengersProjectName, avengerContributionStats)
        and:
            def cooperationProjectName = "cooperation"
            def cooperationUserStats = [contributionStats(HULK, 75L, 7500L, 100L),
                                        contributionStats(BATMAN, 75L, 2500L, 100L)]
            def cooperationProject = createCalculationResult(cooperationProjectName, cooperationUserStats)

            def cooperationStats = [avengersStats, cooperationProject]

        when:
            def report = reportFactory.createFrom(DATE_FROM, cooperationStats)

        then:
            ContributionReportAssertion.assertReport(report)
                    .includeDateFrom(DATE_FROM)
                    .isCalculateAt(clock.instant())
                    .containsTotalContributionStats(165L, 15000L, 220L)
                    .containsProjectStats(avengersProjectName, 15L, 5000L, 20L)
                    .containsProjectStats(cooperationProjectName, 150L, 10_000L, 200L)
                    .containsUserStats(HULK, 90L, 12500L, 120L)
                    .containsUserStats(BATMAN, 75L, 2500L, 100L)
    }

    def "should create report for empty result"() {
        when:
            def report = reportFactory.createFrom(DATE_FROM, [])

        then:
            ContributionReportAssertion.assertReport(report)
                    .includeDateFrom(DATE_FROM)
                    .isCalculateAt(clock.instant())
                    .hasNoStats()
    }

    private def createCalculationResult(String projectName, List<UserContributionStats> contributionStats) {
        new io.github.orczykowski.core.ProjectCalculationStatsResult.Success(new ProjectStats(new Project("git@github.com:orczykowski/${projectName}.git", Set.of()), contributionStats))
    }

    private def contributionStats(final String name,
                                  final Long commits, final Long linesAdded, final Long linesRemoved) {
        new UserContributionStats(new User(name),
                new ContributionStats(commits, linesAdded, linesRemoved),
                ProjectDistributionStats.empty())
    }
}
