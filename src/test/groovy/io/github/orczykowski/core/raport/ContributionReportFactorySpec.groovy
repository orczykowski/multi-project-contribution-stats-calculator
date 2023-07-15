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

class ContributionReportFactorySpec extends Specification {
    private static final def HULK = "hulk"
    private static final def BATMAN = "batman"

    private static final def DATE_FROM = LocalDate.parse("2020-01-01")
    private static final Clock clock = Clock.fixed(Instant.now())

    @Subject
    ContributionReportFactory reportFactory = new ContributionReportFactory(clock)

    def "should create report for calculation result"() {
        given:
            def avengersProjectName = "avengers"

            def avengerContributionStats = [contributionStats(HULK, 5000L, 15L, 20L, 100, 100, 100),
                                            contributionStats(BATMAN, 0, 1L, 0L, 0L, 0, 0)]
            def avengersStats = createCalculationResult(avengersProjectName, avengerContributionStats)
        and:
            def cooperationProjectName = "cooperation"
            def cooperationUserStats = [contributionStats(HULK, 7500L, 75L, 100L, 75, 50, 50),
                                        contributionStats(BATMAN, 2500L, 75L, 100L, 100L, 50, 50)]
            def cooperationProject = createCalculationResult(cooperationProjectName, cooperationUserStats)

            def cooperationStats = [avengersStats, cooperationProject]

        when:
            def report = reportFactory.createFrom(DATE_FROM, cooperationStats)

        then:
            ContributionReportAssertion.assertReport(report)
                    .includeDateFrom(DATE_FROM)
                    .isCalculateAt(clock.instant())
                    .containsTotalContributionStats(15000L, 165L, 220L)
                    .containsProjectStats(avengersProjectName, 5000L, 15L, 20L, avengerContributionStats)
                    .containsProjectStats(cooperationProjectName, 10_000L, 150L, 200L, cooperationUserStats)
                    .containsUserStats(HULK, 12500L, 90L, 120L)
                    .containsUserStats(BATMAN, 2500L, 75L, 100L)
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
                                  final Long lineOfCode, final Long commits, final Long files,
                                  final BigDecimal codeLinesParticipation, final BigDecimal commitsParticipation, final BigDecimal filesParticipation) {
        new UserContributionStats(new User(name),
                new ContributionStats(lineOfCode, commits, files),
                new ProjectDistributionStats(codeLinesParticipation, commitsParticipation, filesParticipation))
    }
}
