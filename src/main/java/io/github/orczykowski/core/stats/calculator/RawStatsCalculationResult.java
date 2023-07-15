package io.github.orczykowski.core.stats.calculator;

import io.github.orczykowski.core.project.Project;

public sealed interface RawStatsCalculationResult permits RawStatsCalculationResult.Failure,
        RawStatsCalculationResult.Success {

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    record Failure(Project project, String error) implements RawStatsCalculationResult {

        @Override
        public boolean isSuccess() {
            return false;
        }
    }

    record Success(String rawStats) implements RawStatsCalculationResult {

        @Override
        public boolean isSuccess() {
            return true;
        }
    }
}
