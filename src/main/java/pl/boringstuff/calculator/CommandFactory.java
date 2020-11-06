package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.infrastructure.command.ExecutableCommand;
import static pl.boringstuff.infrastructure.config.CalculationParamsProvider.getCalculationParams;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandFactory {
  public static ExecutableCommand cloneRepoCommand(final Project project) {
    return ExecutableCommand.newCommand("git")
            .withArgs(List.of("clone", project.getRepositoryUrl()))
            .inDictionary(getCalculationParams().workingDir())
            .build();
  }

  public static ExecutableCommand calculateContributionStatsCommand(final Project project, final LocalDate date) {
    return ExecutableCommand.newCommand("git")
            .withArgs(args(date, project))
            .inDictionary(pathToProjectRepoDir(project))
            .build();
  }

  public static ExecutableCommand removeRepo(final Project project) {
    return ExecutableCommand.newCommand("rm")
            .withArgs(List.of("-rf", pathToProjectRepoDir(project)))
            .inDictionary(getCalculationParams().workingDir())
            .build();
  }

  private static String pathToProjectRepoDir(final Project project) {
    return getCalculationParams().workingDir().concat(project.getName());
  }

  private static List<String> args(final LocalDate date, final Project project) {
    final List<String> args = new ArrayList<>(Arrays.asList("fame", "--hide-progressbar", "--format=csv", "--after=" + date.toString()));
    if (!project.getExcludePaths().isEmpty()) {
      args.add("--exclude=" + project.getJoinedExcludedPaths(","));
    }
    return Collections.unmodifiableList(args);
  }

}
