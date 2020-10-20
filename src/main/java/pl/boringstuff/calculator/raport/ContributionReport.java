package pl.boringstuff.calculator.raport;

import pl.boringstuff.calculator.stats.ContributionStats;
import pl.boringstuff.calculator.stats.ProjectStats;
import pl.boringstuff.calculator.stats.UserContributionStats;

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