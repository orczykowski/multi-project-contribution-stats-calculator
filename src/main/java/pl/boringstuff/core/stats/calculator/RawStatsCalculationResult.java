package pl.boringstuff.core.stats.calculator;

import pl.boringstuff.core.project.Project;

public sealed interface RawStatsCalculationResult permits RawStatsCalculationResult.Failure, RawStatsCalculationResult.Success {

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
