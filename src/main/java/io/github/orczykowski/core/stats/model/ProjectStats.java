package io.github.orczykowski.core.stats.model;

import io.github.orczykowski.core.project.Project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.orczykowski.infrastructure.utils.Preconditions.checkRequiredArgument;

public class ProjectStats {

  private final Project project;
  private final List<UserContributionStats> userStats;
  private final ContributionStats total;

  public ProjectStats(final Project project, final List<UserContributionStats> userStats) {
    checkRequiredArgument(project);
    final List<UserContributionStats> significantStats = getOnlySignificantUserStats(userStats);
    this.project = project;
    this.total = calculateTotal(significantStats);
    this.userStats = computeDistributions(significantStats, this.total);
  }

  public Project project() {
    return project;
  }

  public List<UserContributionStats> userStats() {
    return userStats;
  }

  public ContributionStats total() {
    return total;
  }

  private List<UserContributionStats> getOnlySignificantUserStats(
          final List<UserContributionStats> userStats) {
    return Objects.requireNonNullElse(userStats, new ArrayList<UserContributionStats>())
            .stream()
            .filter(it -> it.counts().isSignificantContribution())
            .collect(Collectors.toUnmodifiableList());
  }

  private ContributionStats calculateTotal(final List<UserContributionStats> userStats) {
    return userStats.stream()
            .map(UserContributionStats::counts)
            .reduce(ContributionStats::add)
            .orElseGet(ContributionStats::empty);
  }

  private List<UserContributionStats> computeDistributions(
          final List<UserContributionStats> stats, final ContributionStats total) {
    if (total.commits() == 0 && total.linesAdded() == 0 && total.linesRemoved() == 0) {
      return stats;
    }
    return stats.stream()
            .map(s -> s.withDistribution(
                    new ProjectDistributionStats(
                            percentage(s.counts().commits(), total.commits()),
                            percentage(s.counts().linesAdded(), total.linesAdded()),
                            percentage(s.counts().linesRemoved(), total.linesRemoved())
                    )))
            .collect(Collectors.toUnmodifiableList());
  }

  private BigDecimal percentage(final Long part, final Long whole) {
    if (whole == 0L) {
      return BigDecimal.ZERO;
    }
    return BigDecimal.valueOf(part)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(whole), 1, RoundingMode.HALF_UP);
  }
}
