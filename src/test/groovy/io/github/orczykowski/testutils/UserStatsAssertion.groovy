package io.github.orczykowski.testutils

import io.github.orczykowski.core.project.User
import io.github.orczykowski.core.stats.model.ContributionStats
import io.github.orczykowski.core.stats.model.ProjectDistributionStats
import io.github.orczykowski.core.stats.model.UserContributionStats

import static ContributionStatsAssertion.assertContributionStats
import static DistributionStatsAssertion.assertDistributionStats

class UserStatsAssertion {

    private final UserContributionStats subject

    private UserStatsAssertion(final UserContributionStats stats) {
        assert stats != null
        subject = stats
    }

    static def assertUserContributionStats(final UserContributionStats stats) {
        new UserStatsAssertion(stats)
    }

    def hasEmptyCountsStats() {
        assertContributionStats(subject.counts()).isEmpty()
        this
    }

    def hasEmptyDistribution() {
        assertDistributionStats(subject.distribution()).isEmpty()
        this
    }

    def hasContributionStats(final ContributionStats stats) {
        assertContributionStats(subject.counts())
                .hasLineOfCode(stats.lineOfCode())
                .hasNumberOfCommits(stats.commits())
                .hasNumberOfFiles(stats.files())
        this
    }

    def hasContributionStats() {
        assertContributionStats(subject.counts(), this)
    }

    def hasDistributionStats(final ProjectDistributionStats stats) {
        assertDistributionStats(subject.distribution())
                .hasPercentOfLineOfCode(stats.codeLinesParticipation())
                .hasPercentOfCommits(stats.commitsParticipation())
                .hasPercentOfFiles(stats.filesParticipation())
        this
    }

    def hasDistributionStats() {
        assertDistributionStats(subject.distribution(), this)
    }

    def hasUser(final User user) {
        assert subject.user() == user
        this
    }
}
