package pl.boringstuff.core.stats.calculator;

import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.boringstuff.core.project.Project;
import pl.boringstuff.infrastructure.command.CommandExecutionResult;
import pl.boringstuff.infrastructure.command.CommandExecutionResult.Failure;
import pl.boringstuff.infrastructure.command.ExecutableSystemCommand;
import pl.boringstuff.infrastructure.executor.Task;
import static pl.boringstuff.infrastructure.utils.Preconditions.checkRequiredArgument;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GitFameContributionRawStatsCalculationTask implements Task<RawStatsCalculationResult> {
  private static final Logger log = LoggerFactory.getLogger(GitFameContributionRawStatsCalculationTask.class);

  private final Project project;
  private final LocalDate dateForm;
  private final String workingDir;

  public GitFameContributionRawStatsCalculationTask(final Project project, final LocalDate dateForm, final String workingDir) {
    checkRequiredArgument(dateForm);
    checkRequiredArgument(dateForm);
    checkRequiredArgument(project);
    this.dateForm = dateForm;
    this.workingDir = workingDir;
    this.project = project;
  }

  public RawStatsCalculationResult run() {
    try {
      if (checkOutRepo() instanceof Failure failure) {
        return new RawStatsCalculationResult.Failure(project, failure.result());
      }
      final var commandExecutionResult = calculateStats();
      return createCalculationResultFrom(commandExecutionResult);
    } finally {
      cleanup();
    }
  }

  private RawStatsCalculationResult createCalculationResultFrom(final CommandExecutionResult result) {
    if (result instanceof Failure failure) {
      return new RawStatsCalculationResult.Failure(project, failure.result());
    }
    return new RawStatsCalculationResult.Success(result.result());
  }

  private CommandExecutionResult checkOutRepo() {
    return ExecutableSystemCommand.newCommand("git")
            .withArgs(List.of("clone", project.getRepositoryUrl()))
            .inDictionary(workingDir)
            .buildSystemCommand()
            .execute();
  }

  private CommandExecutionResult calculateStats() {
    return ExecutableSystemCommand.newCommand("git")
            .withArgs(args(dateForm, project))
            .inDictionary(pathToProjectRepoDir())
            .buildSystemCommand()
            .execute();
  }

  private void cleanup() {
    try {
      final var path = pathToProjectRepoDir();
      FileUtils.delete(new File(path), FileUtils.RECURSIVE);
    } catch (final IOException e) {
      log.error("cannot remove repo dir {}", pathToProjectRepoDir(), e);
    }
  }

  private String pathToProjectRepoDir() {
    return workingDir.concat(project.getName());
  }

  private List<String> args(final LocalDate date, final Project project) {
    final List<String> args = new ArrayList<>(Arrays.asList("fame", "--hide-progressbar", "--format=csv", "--after=" + date.toString()));
    if (!project.getExcludePaths().isEmpty()) {
      args.add("--exclude=" + project.getJoinedExcludedPaths(","));
    }
    return Collections.unmodifiableList(args);
  }
}
