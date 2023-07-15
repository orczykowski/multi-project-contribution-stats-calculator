package io.github.orczykowski.core.stats.model;

import io.github.orczykowski.core.project.User;
import io.github.orczykowski.infrastructure.utils.Preconditions;

import java.util.Objects;

public record UserContributionStats(User user,
                                    ContributionStats counts,
                                    ProjectDistributionStats distribution) implements Comparable {

    public UserContributionStats(final User user, final ContributionStats counts,
                                 final ProjectDistributionStats distribution) {
        Preconditions.checkRequiredArgument(user);
        this.user = user;
        this.counts = Objects.requireNonNullElseGet(counts, ContributionStats::empty);
        this.distribution = Objects.requireNonNullElseGet(distribution,
                ProjectDistributionStats::empty);
    }

    public UserContributionStats add(final UserContributionStats stats) {
        if (!user.equals(stats.user)) {
            throw new IllegalArgumentException("cannot add stats to different users");
        }
        return new UserContributionStats(this.user, this.counts.add(stats.counts),
                ProjectDistributionStats.empty());
    }

    @Override
    public int compareTo(final Object o) {
        if (!(o instanceof UserContributionStats)) {
            return 1;
        }
        return -1 * this.counts.compareTo(((UserContributionStats) o).counts);
    }
}
