package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.infrastructure.command.ExecutableCommand;
import pl.boringstuff.infrastructure.utils.DirectoryHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandFactory {
  public static ExecutableCommand cloneRepoCommand(final Project project) {
    return ExecutableCommand.newCommand("git")
            .withArgs(List.of("clone", project.getRepositoryUrl()))
            .inDictionary(DirectoryHelper.pathToTmpRepoDir())
            .build();
  }

  public static ExecutableCommand calculateContributionStatsCommand(final Project project, final LocalDate date) {
    return ExecutableCommand.newCommand("git")
            .withArgs(args(date, project))
            .inDictionary(DirectoryHelper.pathToProjectRepoDir(project))
            .build();
  }

  public static ExecutableCommand removeRepo(final Project project) {
    return ExecutableCommand.newCommand("rm")
            .withArgs(List.of("-rf", DirectoryHelper.pathToProjectRepoDir(project)))
            .inDictionary(DirectoryHelper.pathToTmpRepoDir())
            .build();
  }

  private static List<String> args(final LocalDate date, final Project project) {
    final List<String> args = new ArrayList<>(Arrays.asList("fame", "--hide-progressbar", "--format=csv", "--after=" + date.toString()));
    if (!project.getExcludePaths().isEmpty()) {
      args.add("--exclude=" + project.getJoinedExcludedPaths(","));
    }
    return Collections.unmodifiableList(args);
  }

}
