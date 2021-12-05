package pl.boringstuff.testutils

import pl.boringstuff.core.raport.ContributionReport
import pl.boringstuff.core.stats.model.UserContributionStats

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

    def containsTotalContributionStats(Long lineOfCode, Long commits, Long files) {
        ContributionStatsAssertion.assertContributionStats(report.totalContribution())
                .hasLineOfCode(lineOfCode)
                .hasNumberOfCommits(commits)
                .hasNumberOfFiles(files)
        this
    }

    def containsProjectStats(String projectName, Long lineOfCode, Long commits, Long files, List<UserContributionStats> userContributionStats) {
        def projectsStats = report.projectContributionStats()
        assert projectsStats != null
        def projectStats = projectsStats.find { it.project().getName() == projectName }
        assert projectStats != null
        ContributionStatsAssertion.assertContributionStats(projectStats.total())
                .hasLineOfCode(lineOfCode)
                .hasNumberOfCommits(commits)
                .hasNumberOfFiles(files)
        projectStats.userStats() == userContributionStats
        this
    }

    def containsUserStats(String userName,
                          Long lineOfCode, Long commits, Long files) {
        def usersStats = report.totalUserContributionStats()
        assert usersStats != null
        def userStats = usersStats.find { it.user().name() == userName }
        assert userStats != null
        ContributionStatsAssertion.assertContributionStats(userStats.counts())
                .hasLineOfCode(lineOfCode)
                .hasNumberOfCommits(commits)
                .hasNumberOfFiles(files)
        this
    }

    def hasNoStats() {
        assert report.projectContributionStats().isEmpty()
        assert report.totalUserContributionStats().isEmpty()
        ContributionStatsAssertion.assertContributionStats(report.totalContribution())
                .isEmpty()
    }
}
