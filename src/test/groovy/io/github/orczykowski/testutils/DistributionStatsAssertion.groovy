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
        hasPercentOfLineOfCode(0.0)
        hasPercentOfCommits(0.0)
        hasPercentOfFiles(0.0)
        this
    }

    def hasPercentOfLineOfCode(final BigDecimal loc) {
        assert subject.codeLinesParticipation() == loc
        this
    }

    def hasPercentOfCommits(final BigDecimal numberOfCommits) {
        assert subject.commitsParticipation() == numberOfCommits
        this
    }


    def hasPercentOfFiles(final BigDecimal numberOfFiles) {
        assert subject.filesParticipation() == numberOfFiles
        this
    }

    UserStatsAssertion and() {
        context
    }
}
