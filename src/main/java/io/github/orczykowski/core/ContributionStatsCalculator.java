package io.github.orczykowski.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.github.orczykowski.core.project.Project;
import io.github.orczykowski.core.project.ProjectRepository;
import io.github.orczykowski.core.raport.ContributionReport;
import io.github.orczykowski.core.raport.ContributionReportFactory;
import io.github.orczykowski.core.stats.calculator.jgit.JGitContributionStatsCalculationTask;
import io.github.orczykowski.infrastructure.executor.ThreadPoolTaskExecutor;

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

  ContributionStatsCalculator(final ThreadPoolTaskExecutor calculationProcessor,
                              final ProjectRepository projectRepository,
                              final CalculationSpecificationSupplier specificationSupplier,
                              final ContributionReportFactory reportFactory) {
    this.calculationProcessor = calculationProcessor;
    this.projectRepository = projectRepository;
    this.specificationSupplier = specificationSupplier;
    this.reportFactory = reportFactory;
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
    final var task = new JGitContributionStatsCalculationTask(project, reportSpec.dateFrom(), null, reportSpec.workingDir());
    return calculationProcessor.sendToProcess(task);
  }
}
