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


  public CalculateContributionStatsTask(final Project project, final LocalDate dateFrom) {
    this.project = project;
    this.dateFrom = dateFrom;
  }

  public ProjectCalculationStatsResult run() {
    try {
      if (checkOutRepo() instanceof Failure failure) {
        return new ProjectCalculationStatsResult.Failure(project, failure.errorMessage());
      }
      final var commandExecutionResult = calculateStats();
      return createCalculationResultFrom(commandExecutionResult);
    } finally {
      cleanup();
    }
  }

  /* TODO rewrite on switch pattern matching (not working yet)
      return switch(result) {
         case CommandExecutionResult.Success success -> createSuccessCalculationResult(success.resultStream());
         case CommandExecutionResult.Failure fail -> new ProjectCalculationStatsResult.Failure(project, fail.error());
    }
   */
  private ProjectCalculationStatsResult createCalculationResultFrom(final CommandExecutionResult result) {
    if (result instanceof Failure failure) {
      return new ProjectCalculationStatsResult.Failure(project, failure.errorMessage());
    }
    return createSuccessCalculationResult((CommandExecutionResult.Success) result);
  }

  private ProjectCalculationStatsResult createSuccessCalculationResult(final CommandExecutionResult.Success successExecution) {
    var projectStats = new ProjectStats(project, RawCsvStatsResultParser.getInstance().parse(successExecution.stringResult()));
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
