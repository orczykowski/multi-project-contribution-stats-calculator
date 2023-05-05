package pl.orczykowski.core.stats.calculator

import pl.orczykowski.core.stats.model.UserContributionStats
import spock.lang.Specification
import spock.lang.Subject

import static pl.orczykowski.testutils.UserStatsAssertion.assertUserContributionStats

class RawCsvStatsResultParserIntSpec extends Specification {

    static def csv = """name,loc,commits,files,distribution
HULK,"7,617",2,11,59.1 /  4.9 / 44.0
BATMAN,"4,535",8,20,35.2 / 19.5 / 80.0
IRON_MAN,619,12,6, 4.8 / 29.3 / 24.0
"""
    @Subject
    GitFameRawCsvStatsResultParser parser = new GitFameRawCsvStatsResultParser()

    def "should parse csv project stats to ProjectStats"() {
        when:
        def result = parser.parse(new RawStatsCalculationResult.Success(csv))

        then:
        result.size() == 3
        // @formatter:off
        and:
        assertUserContributionStats(findUserStats(result, "hulk"))
                .hasContributionStats()
                .hasLineOfCode(7617)
                .hasNumberOfCommits(2)
                .hasNumberOfFiles(11)
                .and()
                .hasDistributionStats()
                .hasPercentOfLineOfCode(59.1)
                .hasPercentOfCommits(4.9)
                .hasPercentOfFiles(44.0)

        and:
        assertUserContributionStats(findUserStats(result, "batman"))
                .hasContributionStats()
                .hasLineOfCode(4535)
                .hasNumberOfCommits(8)
                .hasNumberOfFiles(20)
                .and()
                .hasDistributionStats()
                .hasPercentOfLineOfCode(35.2)
                .hasPercentOfCommits(19.5)
                .hasPercentOfFiles(80.0)

        and:
        assertUserContributionStats(findUserStats(result, "iron_man"))
                .hasContributionStats()
                .hasLineOfCode(619)
                .hasNumberOfCommits(12)
                .hasNumberOfFiles(6)
                .and()
                .hasDistributionStats()
                .hasPercentOfLineOfCode(4.8)
                .hasPercentOfCommits(29.3)
                .hasPercentOfFiles(24.0)
        // @formatter:on
    }

    def "should return empty list for empty csv"() {
        when:
        def result = parser.parse(new RawStatsCalculationResult.Success(invalidCsv))

        then:
        result.isEmpty()

        where:
        invalidCsv << ["", " ", null, "name,loc,commits,files,distribution"]
    }

    private UserContributionStats findUserStats(List<UserContributionStats> stats, String userName) {
        def userStats = stats.find { it.user().name == userName }
        assert userStats != null
        userStats
    }
}
