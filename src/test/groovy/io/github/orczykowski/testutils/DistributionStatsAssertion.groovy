package io.github.orczykowski.testutils


import io.github.orczykowski.core.stats.model.ProjectDistributionStats

class DistributionStatsAssertion {

    private final UserStatsAssertion context
    private final ProjectDistributionStats subject


    private DistributionStatsAssertion(final ProjectDistributionStats stats, UserStatsAssertion context) {
        assert stats != null
        this.subject = stats
        this.context = context
    }

    static def assertDistributionStats(final ProjectDistributionStats stats, UserStatsAssertion context = null) {
        new DistributionStatsAssertion(stats, context)
    }

    def isEmpty() {
        hasPercentOfCommits(0.0)
        hasPercentOfLinesAdded(0.0)
        hasPercentOfLinesRemoved(0.0)
        this
    }

    def hasPercentOfCommits(final BigDecimal commits) {
        assert subject.commitsParticipation() == commits
        this
    }

    def hasPercentOfLinesAdded(final BigDecimal linesAdded) {
        assert subject.linesAddedParticipation() == linesAdded
        this
    }

    def hasPercentOfLinesRemoved(final BigDecimal linesRemoved) {
        assert subject.linesRemovedParticipation() == linesRemoved
        this
    }

    UserStatsAssertion and() {
        context
    }
}
