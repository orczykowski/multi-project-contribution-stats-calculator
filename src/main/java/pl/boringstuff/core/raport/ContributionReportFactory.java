package pl.boringstuff.core.raport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toUnmodifiableList;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.ProjectCalculationStatsResult;
import pl.boringstuff.core.stats.model.ContributionStats;
import pl.boringstuff.core.stats.model.ProjectStats;
import pl.boringstuff.core.stats.model.UserContributionStats;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ContributionReportFactory {
  private final Clock clock;

  ContributionReportFactory(final Clock clock) {
    this.clock = clock;
  }

  public ContributionReport createFrom(final LocalDate dateFrom, final List<ProjectCalculationStatsResult> results) {
    return new ContributionReport(dateFrom, clock.instant(),
            projectContributionStats(results),
            aggregatedUsersStats(results),
            totalStats(results),
            failures(results));
  }

  public ContributionReport earlyCalculationFailReport(final LocalDate dateFrom, final String... failures) {
    return new ContributionReport(dateFrom, Instant.now(), List.of(), List.of(), ContributionStats.empty(), Arrays.asList(failures.clone()));
  }

  private List<String> failures(final List<ProjectCalculationStatsResult> results) {
    return results.stream()
            .filter(it -> it instanceof ProjectCalculationStatsResult.Failure)
            .map(it -> ((ProjectCalculationStatsResult.Failure) it).getErrorMessage())
            .sorted()
            .collect(Collectors.toUnmodifiableList());
  }

  private List<UserContributionStats> aggregatedUsersStats(final List<ProjectCalculationStatsResult> results) {
    return getProjectStatsFrom(results)
            .map(ProjectStats::userStats)
            .flatMap(Collection::stream)
            .collect(groupingBy(UserContributionStats::user, reducing(UserContributionStats::add)))
            .values().stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(isSignificantContribution())
            .sorted(UserContributionStats::compareTo)
            .collect(toUnmodifiableList());
  }

  private Predicate<UserContributionStats> isSignificantContribution() {
    return it -> it.counts().isSignificantContribution();
  }

  private List<ProjectStats> projectContributionStats(final List<ProjectCalculationStatsResult> results) {
    return getProjectStatsFrom(results)
            .sorted(Comparator.comparing(it -> it.project().getName()))
            .collect(Collectors.toUnmodifiableList());
  }

  private ContributionStats totalStats(final List<ProjectCalculationStatsResult> results) {
    return getProjectStatsFrom(results)
            .map(ProjectStats::total)
            .reduce(ContributionStats::add)
            .orElseGet(ContributionStats::empty);
  }

  private Stream<ProjectStats> getProjectStatsFrom(final List<ProjectCalculationStatsResult> results) {
    return results.stream()
            .filter(ProjectCalculationStatsResult::isSuccess)
            .map(it -> ((ProjectCalculationStatsResult.Success) it).stats());
  }
}
