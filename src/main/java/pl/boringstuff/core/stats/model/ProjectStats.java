package pl.boringstuff.core.stats.model;

import pl.boringstuff.core.project.Project;
import static pl.boringstuff.infrastructure.utils.Preconditions.checkRequiredArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectStats {
  private final Project project;
  private final List<UserContributionStats> userStats;
  private final ContributionStats total;

  public ProjectStats(final Project project, final List<UserContributionStats> userStats) {
    checkRequiredArgument(project);
    final List<UserContributionStats> userStats1 = getOnlySignificantUserStats(userStats);
    this.project = project;
    this.userStats = userStats1;
    this.total = calculateTotal(userStats1);
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

  private List<UserContributionStats> getOnlySignificantUserStats(final List<UserContributionStats> userStats) {
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
}
