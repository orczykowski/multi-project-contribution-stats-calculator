package pl.boringstuff.core.raport;

import pl.boringstuff.core.stats.model.ContributionStats;
import pl.boringstuff.core.stats.model.ProjectStats;
import pl.boringstuff.core.stats.model.UserContributionStats;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record ContributionReport(
        LocalDate dateFrom,
        Instant calculationDate,
        List<ProjectStats> projectContributionStats,
        List<UserContributionStats> totalUserContributionStats,
        ContributionStats totalContribution,
        List<String> failures) {

}
