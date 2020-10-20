package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.ProjectRepository;
import pl.boringstuff.calculator.raport.ContributionReport;
import pl.boringstuff.calculator.raport.ContributionReportFactory;
import pl.boringstuff.infrastructure.executor.TaskExecutor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ContributionStatsCalculator {
  private final TaskExecutor taskExecutor;
  private final ProjectRepository projectRepository;
  private final ContributionReportFactory reportFactory;
  private final RawCalculationResultParser rawCalculationResultParser;

  public ContributionStatsCalculator(final TaskExecutor taskExecutor,
                                     final ProjectRepository projectRepository,
                                     final ContributionReportFactory reportFactory,
                                     final RawCalculationResultParser rawCalculationResultParser) {
    this.taskExecutor = taskExecutor;
    this.projectRepository = projectRepository;
    this.reportFactory = reportFactory;
    this.rawCalculationResultParser = rawCalculationResultParser;
  }

  public ContributionReport calculate(final LocalDate dateFrom) {
    try {
      var computableFutures = sendCalculationTaskForAllProjects(dateFrom);
      final var results = getResults(computableFutures);
      return reportFactory.createFrom(dateFrom, results);
    } catch (InterruptedException | ExecutionException | IllegalStateException | IllegalArgumentException ex) {
      ex.printStackTrace();
      return reportFactory.earlyCalculationFailReport(dateFrom, ex.getMessage());
    }
  }

  private List<ProjectCalculationStatsResult> getResults(final CompletableFuture[] computableFutures) throws ExecutionException, InterruptedException {
    return CompletableFuture.allOf(computableFutures)
            .thenApply(it -> joinResults(computableFutures))
            .get();
  }

  private List<ProjectCalculationStatsResult> joinResults(final CompletableFuture[] computableFutures) {
    return Arrays.stream(computableFutures)
            .map(CompletableFuture<ProjectCalculationStatsResult>::join)
            .collect(Collectors.toList());
  }

  private CompletableFuture[] sendCalculationTaskForAllProjects(final LocalDate dateFrom) {
    return projectRepository
            .findAll()
            .map(project -> new CalculateContributionStatsTask(project, dateFrom, rawCalculationResultParser))
            .map(taskExecutor::sendToProcess)
            .toArray(CompletableFuture[]::new);
  }
}
