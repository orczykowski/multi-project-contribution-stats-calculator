package pl.orczykowski.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.orczykowski.core.project.Project;
import pl.orczykowski.core.project.ProjectRepository;
import pl.orczykowski.core.raport.ContributionReport;
import pl.orczykowski.core.raport.ContributionReportFactory;
import pl.orczykowski.core.stats.calculator.GitFameContributionRawStatsCalculationTask;
import pl.orczykowski.core.stats.calculator.GitFameRawCsvStatsResultParser;
import pl.orczykowski.core.stats.calculator.RawStatsCalculationResult;
import pl.orczykowski.core.stats.model.ProjectStats;
import pl.orczykowski.infrastructure.executor.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Component
class ContributionStatsCalculator {

  private final Logger log = LoggerFactory.getLogger(ContributionStatsCalculator.class);

  private final CalculatorTaskProcessor calculationProcessor;
  private final ProjectRepository projectRepository;
  private final CalculationSpecificationSupplier specificationSupplier;

  private final ContributionReportFactory reportFactory;
  private final GitFameRawCsvStatsResultParser rawCalculationResultParser;

  ContributionStatsCalculator(final ThreadPoolTaskExecutor calculationProcessor,
                              final ProjectRepository projectRepository,
                              final CalculationSpecificationSupplier specificationSupplier,
                              final ContributionReportFactory reportFactory,
                              final GitFameRawCsvStatsResultParser rawCalculationResultParser) {
    this.calculationProcessor = calculationProcessor;
    this.projectRepository = projectRepository;
    this.specificationSupplier = specificationSupplier;
    this.reportFactory = reportFactory;
    this.rawCalculationResultParser = rawCalculationResultParser;
  }

  public ContributionReport calculate() {
    final var reportSpec = specificationSupplier.supply();

    log.info("Starting calculations for {} projects", projectRepository.count());
    try {
      final var computableFutures = sendCalculationTaskForAllProjects();
      final var results = getResults(computableFutures, reportSpec.timoutInSeconds());
      return reportFactory.createFrom(reportSpec.dateFrom(), results);
    } catch (InterruptedException ex) {
      log.error("processing has been interrupted, [run params: {}]", reportSpec, ex);
      return reportFactory.earlyCalculationFailReport(reportSpec.dateFrom(), ex.getMessage());
    } catch (ExecutionException ex) {
      log.error("Problem with task execution, [run params: {}]", reportSpec, ex);
      return reportFactory.earlyCalculationFailReport(reportSpec.dateFrom(), ex.getMessage());
    } catch (TimeoutException ex) {
      log.error("Task is to long, increase task timeout, [run params: {}]", reportSpec, ex);
      return reportFactory.earlyCalculationFailReport(reportSpec.dateFrom(), ex.getMessage());
    }
  }

  private List<ProjectCalculationStatsResult> getResults(
          final CompletableFuture[] computableFutures, final Long timeout)
          throws ExecutionException, InterruptedException, TimeoutException {
    return CompletableFuture.allOf(computableFutures)
            .thenApply(it -> joinResults(computableFutures))
            .get(timeout, TimeUnit.SECONDS);
  }

  private List<ProjectCalculationStatsResult> joinResults(
          final CompletableFuture[] computableFutures) {
    return Arrays.stream(computableFutures)
            .map(CompletableFuture<ProjectCalculationStatsResult>::join)
            .collect(Collectors.toList());
  }

  private CompletableFuture[] sendCalculationTaskForAllProjects() {
    return projectRepository
            .findAll()
            .map(this::calculateProjectStats)
            .toArray(CompletableFuture[]::new);
  }

  private CompletableFuture<ProjectCalculationStatsResult> calculateProjectStats(final Project project) {
    final var reportSpec = specificationSupplier.supply();
    final var task = new GitFameContributionRawStatsCalculationTask(project, reportSpec.dateFrom(), reportSpec.workingDir());
    return calculationProcessor.sendToProcess(task)
            .thenApply(it -> parseResult(project, it));
  }

  private ProjectCalculationStatsResult parseResult(final Project project, final RawStatsCalculationResult result) {
    if (result.isFailure()) {
      final var failure = (RawStatsCalculationResult.Failure) result;
      return new ProjectCalculationStatsResult.Failure(failure.project(), failure.error());
    }
    final ProjectStats stats = new ProjectStats(project, chooseCorrectParser().parse((RawStatsCalculationResult.Success) result));
    return new ProjectCalculationStatsResult.Success(stats);
  }

  private GitFameRawCsvStatsResultParser chooseCorrectParser() {
    return this.rawCalculationResultParser;
  }
}
