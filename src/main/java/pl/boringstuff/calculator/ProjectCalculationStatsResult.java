package pl.boringstuff.calculator;

import pl.boringstuff.calculator.project.Project;
import pl.boringstuff.calculator.stats.ProjectStats;

import java.util.Optional;

public sealed interface ProjectCalculationStatsResult permits ProjectCalculationStatsResult.Failure, ProjectCalculationStatsResult.Success {


  record Failure(Project project, String error) implements ProjectCalculationStatsResult {
    public String getErrorMessage() {
      var projectName = Optional.ofNullable(project).map(Project::getName).orElse("<?>");
      return "Problem with calculate report for: [%s], reason: [%s]".formatted(projectName, error);
    }
  }

  record Success(ProjectStats stats) implements ProjectCalculationStatsResult {
  }
}
