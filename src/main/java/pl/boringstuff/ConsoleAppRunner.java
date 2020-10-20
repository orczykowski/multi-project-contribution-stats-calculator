package pl.boringstuff;

import pl.boringstuff.calculator.ContributionStatsCalculator;
import pl.boringstuff.calculator.RawCsvStatsResultParser;
import pl.boringstuff.calculator.project.JsonFileProjectRepository;
import pl.boringstuff.calculator.raport.ContributionReportFactory;
import pl.boringstuff.calculator.raport.writer.ReportWriterProvider;
import pl.boringstuff.infrastructure.ConsoleArgsParser;
import pl.boringstuff.infrastructure.ShootDownCleaner;
import pl.boringstuff.infrastructure.executor.TaskExecutor;

public class ConsoleAppRunner {
  public static void main(String[] args) {

    final var argsParser = new ConsoleArgsParser();
    final var parameters = argsParser.parse(args);

    final var rawResultParser = RawCsvStatsResultParser.getInstance();
    final var executor = TaskExecutor.getInstance();
    final var repo = JsonFileProjectRepository.getInstance();
    final var reportFactory = new ContributionReportFactory();

    var report = new ContributionStatsCalculator(executor, repo, reportFactory, rawResultParser)
            .calculate(parameters.dateFrom());

    ReportWriterProvider.provideFor(parameters.reportFormat()).write(report);
    ShootDownCleaner.cleanup();
  }
}
