package pl.boringstuff;

import pl.boringstuff.calculator.ContributionStatsCalculator;
import pl.boringstuff.calculator.project.JsonFileProjectRepository;
import pl.boringstuff.calculator.raport.ContributionReportFactory;
import pl.boringstuff.calculator.raport.writer.HtmlWriter;
import pl.boringstuff.infrastructure.ConsoleArgsParser;
import pl.boringstuff.infrastructure.ShootDownCleaner;
import pl.boringstuff.infrastructure.executor.TaskExecutor;

public class ConsoleAppRunner {
  public static void main(String[] args) {
    final var argsParser = new ConsoleArgsParser();

    final var executor = TaskExecutor.getInstance();
    final var repo = JsonFileProjectRepository.getInstance();
    final var reportFactory = new ContributionReportFactory();

    var report = new ContributionStatsCalculator(executor, repo, reportFactory)
            .calculate(argsParser.parse(args));

    new HtmlWriter().write(report);
    ShootDownCleaner.cleanup();
  }

}
