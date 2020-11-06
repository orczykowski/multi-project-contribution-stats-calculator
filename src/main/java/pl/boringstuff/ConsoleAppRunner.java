package pl.boringstuff;

import pl.boringstuff.calculator.ContributionStatsCalculator;
import pl.boringstuff.calculator.RawCsvStatsResultParser;
import pl.boringstuff.calculator.project.JsonFileProjectRepository;
import pl.boringstuff.calculator.raport.ContributionReportFactory;
import pl.boringstuff.calculator.raport.writer.ReportWriterProvider;
import pl.boringstuff.infrastructure.ShootDownCleaner;
import pl.boringstuff.infrastructure.config.CalculationParamsProvider;
import static pl.boringstuff.infrastructure.config.CalculationParamsProvider.getCalculationParams;
import pl.boringstuff.infrastructure.executor.TaskExecutor;

public class ConsoleAppRunner {

  public static void main(String[] args) {

    CalculationParamsProvider.init(args);
    final var contributionStatsCalculator = createContributionStatsCalculator();

    var report = contributionStatsCalculator
            .calculate(getCalculationParams().dateFrom());

    ReportWriterProvider.provideFor(getCalculationParams().reportFormat())
            .write(report);

    ShootDownCleaner.cleanup();
  }

  private static ContributionStatsCalculator createContributionStatsCalculator() {
    final var rawResultParser = RawCsvStatsResultParser.getInstance();
    final var executor = TaskExecutor.getInstance();
    final var repo = JsonFileProjectRepository.getInstance();
    final var reportFactory = new ContributionReportFactory();

    return new ContributionStatsCalculator(executor, repo, reportFactory, rawResultParser);
  }
}
