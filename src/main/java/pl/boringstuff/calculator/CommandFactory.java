package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.infrastructure.command.Command;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandFactory {
  private static final String CURRENT_DIR = Paths.get("").toAbsolutePath().toString().concat("/tmp_repos");

  public static Command cloneRepoCommand(final Project project) {
    return Command.newCommand("git")
            .withArgs(List.of("clone", project.getRepositoryUrl()))
            .inDictionary(tmpRepositoryDir())
            .build();
  }

  public static Command calculateContributionStatsCommand(final Project project, final LocalDate date) {
    return Command.newCommand("git")
            .withArgs(args(date, project))
            .inDictionary(projectPath(project))
            .build();
  }

  public static Command removeRepo(final Project project) {
    return Command.newCommand("rm")
            .withArgs(List.of("-rf", projectPath(project)))
            .inDictionary(tmpRepositoryDir())
            .build();
  }

  private static List<String> args(final LocalDate date, final Project project) {
    final List<String> args = new ArrayList<>(Arrays.asList("fame", "--hide-progressbar", "--format=csv", "--after=" + date.toString()));
    if (!project.getExcludePaths().isEmpty()) {
      String paths = String.join(",", project.getExcludePaths());
      args.add("--exclude=" + paths);
    }
    return Collections.unmodifiableList(args);
  }

  private static String tmpRepositoryDir() {
    return CURRENT_DIR;
  }

  private static String projectPath(final Project project) {
    return tmpRepositoryDir() + "/" + project.getName();
  }
}
