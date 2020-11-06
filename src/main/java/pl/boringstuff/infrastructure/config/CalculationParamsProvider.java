package pl.boringstuff.infrastructure.config;

import pl.boringstuff.calculator.project.ReportFormat;
import pl.boringstuff.infrastructure.utils.Preconditions;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;

public class CalculationParamsProvider {

  private final CalculationParameters parameters;

  private static CalculationParamsProvider INSTANCE;

  private CalculationParamsProvider(final CalculationParameters parameters) {
    this.parameters = parameters;
  }

  public static void init(final String[] args) {
    synchronized (CalculationParamsProvider.class) {
      if (Objects.isNull(INSTANCE)) {
        final var parser = ConsoleArgsParser.getInstance();
        INSTANCE = new CalculationParamsProvider(parser.parse(args));
      }
    }
  }

  public static CalculationParamsProvider getCalculationParams() {
    return INSTANCE;
  }

  public LocalDate dateFrom() {
    return getProperty(parameters::dateFrom);
  }

  public String resultDir() {
    return getProperty(parameters::resultDir);
  }

  public String repoPath() {
    return getProperty(parameters::repoPath);
  }

  public ReportFormat reportFormat() {
    return getProperty(parameters::reportFormat);
  }

  public String workingDir() {
    return getProperty(parameters::workingDir);
  }

  private <T> T getProperty(Supplier<T> extractor) {
    Preconditions.checkState(Objects::nonNull, INSTANCE);
    return extractor.get();
  }
}
