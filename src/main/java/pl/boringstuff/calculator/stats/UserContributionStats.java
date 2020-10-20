package pl.boringstuff.calculator.stats;

import pl.boringstuff.calculator.project.User;
import pl.boringstuff.infrastructure.utils.Preconditions;

public record UserContributionStats(User user,
                                    ContributionStats counts,
                                    UserProjectDistributionStats distribution) implements Comparable {

  public UserContributionStats {
    Preconditions.checkRequiredArgument(user);
  }

  public UserContributionStats addByUser(final UserContributionStats stats) {
    if (!user.equals(stats.user)) {
      throw new IllegalStateException("cannot add stats to different users");
    }
    return add(stats);
  }

  public UserContributionStats add(final UserContributionStats stats) {
    return new UserContributionStats(this.user, this.counts.add(stats.counts), UserProjectDistributionStats.empty());
  }

  @Override
  public int compareTo(final Object o) {
    if (!(o instanceof UserContributionStats)) {
      return 1;
    }
    return -1 * this.counts.compareTo(((UserContributionStats) o).counts);
  }
}
