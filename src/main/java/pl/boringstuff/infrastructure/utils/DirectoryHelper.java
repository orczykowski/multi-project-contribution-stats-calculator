package pl.boringstuff.infrastructure.utils;

import pl.boringstuff.calculator.project.Project;

import java.nio.file.Paths;

public class DirectoryHelper {
  private static final String DEFAULT_WORKING_DIR = Paths.get("").toAbsolutePath().toString().concat("/tmp_repos/");

  public static String pathToTmpRepoDir() {
    return DEFAULT_WORKING_DIR;
  }

  public static String pathToProjectRepoDir(final Project project) {
    return pathToTmpRepoDir() + project.getName();
  }
}
