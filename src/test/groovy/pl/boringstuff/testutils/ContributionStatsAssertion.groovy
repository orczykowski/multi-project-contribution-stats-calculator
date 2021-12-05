package pl.boringstuff.testutils


import pl.boringstuff.core.stats.model.ContributionStats

class ContributionStatsAssertion {
    private final UserStatsAssertion context

    private final ContributionStats subject

    private ContributionStatsAssertion(final ContributionStats stats, UserStatsAssertion context) {
        assert stats != null
        subject = stats
        this.context = context
    }

    static def assertContributionStats(final ContributionStats stats, UserStatsAssertion context = null) {
        new ContributionStatsAssertion(stats, context)
    }

    def isEmpty() {
        hasLineOfCode(0L)
        hasNumberOfCommits(0L)
        hasNumberOfFiles(0L)
        this
    }

    def hasLineOfCode(final Long loc) {
        assert subject.lineOfCode() == loc
        this
    }

    def hasNumberOfCommits(final Long numberOfCommits) {
        assert subject.commits() == numberOfCommits
        this
    }

    def hasNumberOfFiles(final Long numberOfFiles) {
        assert subject.files() == numberOfFiles
        this
    }

    UserStatsAssertion and() {
        context
    }
}
