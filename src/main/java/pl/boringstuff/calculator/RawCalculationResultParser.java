package pl.boringstuff.calculator;

import pl.boringstuff.calculator.stats.UserContributionStats;

import java.util.List;

public interface RawCalculationResultParser {
  List<UserContributionStats> parse(String rawResult);
}
