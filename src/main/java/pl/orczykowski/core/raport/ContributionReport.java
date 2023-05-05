package pl.orczykowski.core.raport;

import pl.orczykowski.core.stats.model.ContributionStats;
import pl.orczykowski.core.stats.model.ProjectStats;
import pl.orczykowski.core.stats.model.UserContributionStats;

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
