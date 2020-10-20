package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.calculator.stats.ProjectStats;
import pl.boringstuff.infrastructure.command.CommandExecutionResult;
import pl.boringstuff.infrastructure.command.CommandExecutionResult.Failure;
import pl.boringstuff.infrastructure.executor.Task;

import java.time.LocalDate;


class CalculateContributionStatsTask implements Task<ProjectCalculationStatsResult> {
  private final Project project;
  private final LocalDate dateFrom;
  private final RawCalculationResultParser rawResultParser;


  public CalculateContributionStatsTask(final Project project, final LocalDate dateFrom, final RawCalculationResultParser rawResultParser) {
    this.project = project;
    this.dateFrom = dateFrom;
    this.rawResultParser = rawResultParser;
  }

  public ProjectCalculationStatsResult run() {
    try {
      if (checkOutRepo() instanceof Failure failure) {
        return new ProjectCalculationStatsResult.Failure(project, failure.result());
      }
      final var commandExecutionResult = calculateStats();
      return createCalculationResultFrom(commandExecutionResult);
    } finally {
      cleanup();
    }
  }

  private ProjectCalculationStatsResult createCalculationResultFrom(final CommandExecutionResult result) {
    if (result instanceof Failure failure) {
      return new ProjectCalculationStatsResult.Failure(project, failure.result());
    }
    return createSuccessCalculationResult((CommandExecutionResult.Success) result);
  }

  private ProjectCalculationStatsResult createSuccessCalculationResult(final CommandExecutionResult.Success successExecution) {
    var projectStats = new ProjectStats(project, rawResultParser.parse(successExecution.stringResult()));
    return new ProjectCalculationStatsResult.Success(projectStats);
  }

  private CommandExecutionResult checkOutRepo() {
    return CommandFactory.cloneRepoCommand(project).execute();
  }

  private CommandExecutionResult calculateStats() {
    return CommandFactory.calculateContributionStatsCommand(project, dateFrom).execute();
  }

  private CommandExecutionResult cleanup() {
    return CommandFactory.removeRepo(project).execute();
  }
}
