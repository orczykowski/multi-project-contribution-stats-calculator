package pl.boringstuff.calculator.stats;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.infrastructure.utils.Preconditions;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record ProjectStats(Project project,
                           List<UserContributionStats> userStats,
                           ContributionStats total) implements Comparable {

  public ProjectStats {
    Preconditions.checkRequiredArgument(project);
  }

  public ProjectStats(final Project project, final List<UserContributionStats> userStats) {
    this(project, getUserStats(userStats), calculateTotal(userStats));
  }

  private static List<UserContributionStats> getUserStats(final List<UserContributionStats> userStats) {
    if (Objects.isNull(userStats)) {
      return List.of();
    }
    return userStats.stream()
            .filter(it -> it.counts().isSignificantContribution())
            .collect(Collectors.toUnmodifiableList());
  }

  private static ContributionStats calculateTotal(final List<UserContributionStats> userStats) {
    return userStats.stream()
            .map(UserContributionStats::counts)
            .reduce(ContributionStats::add)
            .orElseGet(ContributionStats::empty);
  }

  @Override
  public int compareTo(final Object o) {
    if (!(o instanceof ProjectStats)) {
      return 1;
    }
    return total.compareTo(((ProjectStats) o).total);
  }
}
