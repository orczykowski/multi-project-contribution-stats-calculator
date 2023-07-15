package io.github.orczykowski.core.project;

import io.github.orczykowski.infrastructure.utils.Preconditions;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class Project {

  private static final String GIT_EXTENSION = ".git";
  private static final String EMPTY = "";

  private final String name;
  private final Set<String> excludePaths;
  private final String repositoryUrl;

  public Project(final String repositoryUrl, final Set<String> excludePaths) {
    Preconditions.checkNotBlankArgument(repositoryUrl);
    this.excludePaths = Objects.requireNonNullElse(excludePaths, Collections.emptySet());
    this.repositoryUrl = repositoryUrl;
    this.name = getNameFrom(repositoryUrl);
  }

  public String getName() {
    return name;
  }

  public String getJoinedExcludedPaths(final String delimiter) {
    return String.join(delimiter, excludePaths);
  }

  public Set<String> getExcludePaths() {
    return excludePaths;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }

  private String getNameFrom(final String repositoryUrl) {
    var splitUrl = repositoryUrl.toLowerCase().split("/");
    assertSplitUrl(splitUrl);
    return splitUrl[splitUrl.length - 1]
            .replace(GIT_EXTENSION, EMPTY);
  }

  private void assertSplitUrl(final String[] splitUrl) {
    Preconditions.checkState(Objects::nonNull, splitUrl);
    Preconditions.checkState((array) -> array.length > 1, splitUrl);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Project project = (Project) o;
    return Objects.equals(name, project.name) &&
            Objects.equals(repositoryUrl, project.repositoryUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, repositoryUrl);
  }

}
