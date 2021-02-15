package pl.boringstuff.domain.stats.model;

import java.util.Objects;


public record ContributionStats(Long lineOfCode, Long commits, Long files) implements Comparable {
  public ContributionStats(final Long lineOfCode, final Long commits, final Long files) {
    this.lineOfCode = normalize(lineOfCode);
    this.commits = normalize(commits);
    this.files = normalize(files);
  }

  public static ContributionStats empty() {
    return new ContributionStats(0L, 0L, 0L);
  }

  public ContributionStats add(final ContributionStats stats) {
    return new ContributionStats(
            this.lineOfCode + stats.lineOfCode,
            this.commits + stats.commits,
            this.files + stats.files);
  }

  public boolean isSignificantContribution() {
    return lineOfCode() > 0;
  }

  private Long normalize(final Long value) {
    return Math.max(Objects.requireNonNullElse(value, 0L), 0L);
  }

  @Override
  public int compareTo(final Object o) {
    if (!(o instanceof ContributionStats)) {
      return 1;
    }
    var toCompare = (ContributionStats) o;
    var compareLines = this.lineOfCode.compareTo(toCompare.lineOfCode());
    if (compareLines != 0) {
      return compareLines;
    }
    var compareCommits = this.commits.compareTo(toCompare.commits());
    if (compareCommits != 0) {
      return compareCommits;
    }
    return this.files.compareTo(toCompare.files);
  }
}
