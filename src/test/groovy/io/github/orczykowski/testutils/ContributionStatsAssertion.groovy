package io.github.orczykowski.testutils


import io.github.orczykowski.core.stats.model.ContributionStats

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
        hasCommits(0L)
        hasLinesAdded(0L)
        hasLinesRemoved(0L)
        hasFilesChanged(0L)
        hasNewFiles(0L)
        hasProductionLinesAdded(0L)
        hasTestLinesAdded(0L)
        hasConfigLinesAdded(0L)
        this
    }

    def hasCommits(final Long commits) {
        assert subject.commits() == commits
        this
    }

    def hasLinesAdded(final Long linesAdded) {
        assert subject.linesAdded() == linesAdded
        this
    }

    def hasLinesRemoved(final Long linesRemoved) {
        assert subject.linesRemoved() == linesRemoved
        this
    }

    def hasFilesChanged(final Long filesChanged) {
        assert subject.filesChanged() == filesChanged
        this
    }

    def hasNewFiles(final Long newFiles) {
        assert subject.newFiles() == newFiles
        this
    }

    def hasProductionLinesAdded(final Long productionLinesAdded) {
        assert subject.productionLinesAdded() == productionLinesAdded
        this
    }

    def hasTestLinesAdded(final Long testLinesAdded) {
        assert subject.testLinesAdded() == testLinesAdded
        this
    }

    def hasConfigLinesAdded(final Long configLinesAdded) {
        assert subject.configLinesAdded() == configLinesAdded
        this
    }

    UserStatsAssertion and() {
        context
    }
}
