package io.github.orczykowski.core.stats.model;

import java.util.Objects;


public record ContributionStats(Long commits, Long linesAdded, Long linesRemoved,
                                Long filesChanged, Long newFiles,
                                Long productionLinesAdded, Long testLinesAdded,
                                Long configLinesAdded) implements Comparable {

  public ContributionStats(final Long commits, final Long linesAdded,
                           final Long linesRemoved, final Long filesChanged,
                           final Long newFiles, final Long productionLinesAdded,
                           final Long testLinesAdded, final Long configLinesAdded) {
    this.commits = normalize(commits);
    this.linesAdded = normalize(linesAdded);
    this.linesRemoved = normalize(linesRemoved);
    this.filesChanged = normalize(filesChanged);
    this.newFiles = normalize(newFiles);
    this.productionLinesAdded = normalize(productionLinesAdded);
    this.testLinesAdded = normalize(testLinesAdded);
    this.configLinesAdded = normalize(configLinesAdded);
  }

  public ContributionStats(final Long commits, final Long linesAdded, final Long linesRemoved) {
    this(commits, linesAdded, linesRemoved, 0L, 0L, 0L, 0L, 0L);
  }

  public static ContributionStats empty() {
    return new ContributionStats(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
  }

  public ContributionStats add(final ContributionStats stats) {
    return new ContributionStats(
            this.commits + stats.commits,
            this.linesAdded + stats.linesAdded,
            this.linesRemoved + stats.linesRemoved,
            this.filesChanged + stats.filesChanged,
            this.newFiles + stats.newFiles,
            this.productionLinesAdded + stats.productionLinesAdded,
            this.testLinesAdded + stats.testLinesAdded,
            this.configLinesAdded + stats.configLinesAdded);
  }

  public boolean isSignificantContribution() {
    return linesAdded() > 0 || linesRemoved() > 0;
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
    var compareLinesAdded = this.linesAdded.compareTo(toCompare.linesAdded());
    if (compareLinesAdded != 0) {
      return compareLinesAdded;
    }
    var compareCommits = this.commits.compareTo(toCompare.commits());
    if (compareCommits != 0) {
      return compareCommits;
    }
    return this.linesRemoved.compareTo(toCompare.linesRemoved);
  }
}
