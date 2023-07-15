package io.github.orczykowski.testutils

import io.github.orczykowski.core.raport.ContributionReport
import io.github.orczykowski.core.stats.model.UserContributionStats

import java.time.Instant
import java.time.LocalDate

class ContributionReportAssertion {
    private final ContributionReport report

    private ContributionReportAssertion(ContributionReport report) {
        this.report = report
    }

    static def assertReport(ContributionReport report) {
        assert report != null
        new ContributionReportAssertion(report)
    }

    def isCalculateAt(Instant calculateTimestamp) {
        assert report.calculationDate() == calculateTimestamp
        this
    }

    def includeDateFrom(LocalDate date) {
        assert report.dateFrom() == date
        this
    }

    def containsTotalContributionStats(Long commits, Long linesAdded, Long linesRemoved,
                                        Long filesChanged = 0L, Long newFiles = 0L,
                                        Long productionLinesAdded = 0L, Long testLinesAdded = 0L,
                                        Long configLinesAdded = 0L) {
        ContributionStatsAssertion.assertContributionStats(report.totalContribution())
                .hasCommits(commits)
                .hasLinesAdded(linesAdded)
                .hasLinesRemoved(linesRemoved)
                .hasFilesChanged(filesChanged)
                .hasNewFiles(newFiles)
                .hasProductionLinesAdded(productionLinesAdded)
                .hasTestLinesAdded(testLinesAdded)
                .hasConfigLinesAdded(configLinesAdded)
        this
    }

    def containsProjectStats(String projectName, Long commits, Long linesAdded, Long linesRemoved,
                             Long filesChanged = 0L, Long newFiles = 0L,
                             Long productionLinesAdded = 0L, Long testLinesAdded = 0L,
                             Long configLinesAdded = 0L) {
        def projectsStats = report.projectContributionStats()
        assert projectsStats != null
        def projectStats = projectsStats.find { it.project().getName() == projectName }
        assert projectStats != null
        ContributionStatsAssertion.assertContributionStats(projectStats.total())
                .hasCommits(commits)
                .hasLinesAdded(linesAdded)
                .hasLinesRemoved(linesRemoved)
                .hasFilesChanged(filesChanged)
                .hasNewFiles(newFiles)
                .hasProductionLinesAdded(productionLinesAdded)
                .hasTestLinesAdded(testLinesAdded)
                .hasConfigLinesAdded(configLinesAdded)
        this
    }

    def containsUserStats(String userName,
                          Long commits, Long linesAdded, Long linesRemoved,
                          Long filesChanged = 0L, Long newFiles = 0L,
                          Long productionLinesAdded = 0L, Long testLinesAdded = 0L,
                          Long configLinesAdded = 0L) {
        def usersStats = report.totalUserContributionStats()
        assert usersStats != null
        def userStats = usersStats.find { it.user().name() == userName }
        assert userStats != null
        ContributionStatsAssertion.assertContributionStats(userStats.counts())
                .hasCommits(commits)
                .hasLinesAdded(linesAdded)
                .hasLinesRemoved(linesRemoved)
                .hasFilesChanged(filesChanged)
                .hasNewFiles(newFiles)
                .hasProductionLinesAdded(productionLinesAdded)
                .hasTestLinesAdded(testLinesAdded)
                .hasConfigLinesAdded(configLinesAdded)
        this
    }

    def hasNoStats() {
        assert report.projectContributionStats().isEmpty()
        assert report.totalUserContributionStats().isEmpty()
        ContributionStatsAssertion.assertContributionStats(report.totalContribution())
                .isEmpty()
    }
}
