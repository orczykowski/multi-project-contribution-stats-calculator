package io.github.orczykowski.core;

import io.github.orczykowski.core.project.Project;
import io.github.orczykowski.core.stats.model.ProjectStats;

import java.util.Optional;

public sealed interface ProjectCalculationStatsResult permits ProjectCalculationStatsResult.Failure,
        ProjectCalculationStatsResult.Success {

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    record Failure(Project project, String error) implements ProjectCalculationStatsResult {

        public String getErrorMessage() {
            var projectName = Optional.ofNullable(project)
                    .map(Project::getName)
                    .orElse("<? UNKNOWN PROJECT ?>");
            return "Problem with calculate report for: [%s], reason: [%s]".formatted(projectName, error);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    }

    record Success(ProjectStats stats) implements ProjectCalculationStatsResult {

        @Override
        public boolean isSuccess() {
            return true;
        }
    }
}
