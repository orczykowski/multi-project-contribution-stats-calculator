package io.github.orczykowski.core.stats.model


import io.github.orczykowski.core.project.User
import io.github.orczykowski.testutils.UserStatsAssertion
import spock.lang.Specification

class UserContributionStatsSpec extends Specification {

    def static final ALL_ONE_CONTRIBUTION_STAT = createContributionStat(1, 1, 1)
    def static final ALL_ONE_DIST_STAT = createDistributionStat(1.0, 1.0, 1.0)

    def static final USER_HULK = new User("hulk")
    def static final USER_BATMAN = new User("batman")

    def "should throw exception when user is not pass"() {
        when:
        new UserContributionStats(null, null, null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should set stats as empty when is not pass"() {
        when:
        def userStats = new UserContributionStats(USER_HULK, null, null)

        then:
        UserStatsAssertion.assertUserContributionStats(userStats)
                .hasUser(USER_HULK)
                .hasEmptyCountsStats()
                .hasEmptyDistribution()
    }

    def "should create user stats with all possible stats"() {
        given:
        def contributionStats = createContributionStat(101, 767, 3)
        def dictStats = createDistributionStat(11.0, 15.1, 1.0)

        when:
        def userStats = new UserContributionStats(USER_HULK, contributionStats, dictStats)

        then:
        UserStatsAssertion.assertUserContributionStats(userStats)
                .hasContributionStats(contributionStats)
                .hasDistributionStats(dictStats)
    }

    def "should compare user stats using reverse counts contribution stats"() {
        when:
        def hulk = createUserContributionStats(USER_HULK, hulkContributionStats, hulkDistributionStats)
        and:
        def batman = createUserContributionStats(USER_BATMAN, ALL_ONE_CONTRIBUTION_STAT, ALL_ONE_DIST_STAT)

        then:
        def result = batman <=> hulk

        then:
        result == expectedResult

        where:
        hulkContributionStats           | hulkDistributionStats                 || expectedResult
        createContributionStat(2, 1, 1) | ALL_ONE_DIST_STAT                     || 1
        createContributionStat(1, 2, 1) | ALL_ONE_DIST_STAT                     || 1
        createContributionStat(1, 1, 2) | ALL_ONE_DIST_STAT                     || 1
        ALL_ONE_CONTRIBUTION_STAT       | ALL_ONE_DIST_STAT                     || 0
        ALL_ONE_CONTRIBUTION_STAT       | createDistributionStat(2.0, 1.0, 1.0) || 0
        ALL_ONE_CONTRIBUTION_STAT       | createDistributionStat(1.0, 2.0, 1.0) || 0
        ALL_ONE_CONTRIBUTION_STAT       | createDistributionStat(1.0, 1.0, 2.0) || 0
    }

    def "should add count user stats when adding two user stats"() {
        given:
        def someContributionStats = createContributionStat(55, 43, 14)

        and:
        def avengersProject = new UserContributionStats(USER_HULK, ALL_ONE_CONTRIBUTION_STAT, ALL_ONE_DIST_STAT)

        and:
        def incredibleHulkProject = new UserContributionStats(USER_HULK, someContributionStats, ALL_ONE_DIST_STAT)
        when:
        def result = avengersProject.add(incredibleHulkProject)

        then:
        UserStatsAssertion.assertUserContributionStats(result)
                .hasUser(USER_HULK)
                .hasContributionStats(new ContributionStats(56, 44, 15))
    }

    def "should set as empty distribution stats when add two user stats"() {
        given:
        def someContributionStats = createContributionStat(10)

        and:
        def avengersProject = new UserContributionStats(USER_HULK, someContributionStats, ALL_ONE_DIST_STAT)

        and:
        def incredibleHulkProject = new UserContributionStats(USER_HULK, someContributionStats, ALL_ONE_DIST_STAT)
        when:
        def result = avengersProject.add(incredibleHulkProject)

        then:
        UserStatsAssertion.assertUserContributionStats(result)
                .hasUser(USER_HULK)
                .hasEmptyDistribution()
    }

    def "should throw exception when try add two user stats with different users"() {
        given:
        def hulkStats = createUserContributionStats(USER_HULK)
        def batmanStats = createUserContributionStats(USER_BATMAN)

        when:
        hulkStats.add(batmanStats)

        then:
        thrown(IllegalArgumentException)
    }

    private static UserContributionStats createUserContributionStats(final User user,
                                                                     final ContributionStats contributionStats = ALL_ONE_CONTRIBUTION_STAT,
                                                                     final ProjectDistributionStats distributionStats = ALL_ONE_DIST_STAT) {
        new UserContributionStats(user, contributionStats, distributionStats)
    }

    private static ContributionStats createContributionStat(Long loc = 1, Long commits = 0, Long files = 0) {
        new ContributionStats(loc, commits, files)
    }

    private static ProjectDistributionStats createDistributionStat(BigDecimal loc = 1, BigDecimal commits = 0.0, BigDecimal files = 0.0) {
        new ProjectDistributionStats(loc, commits, files)
    }
}
