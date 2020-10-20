package pl.boringstuff.calculator.raport.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.boringstuff.calculator.project.Project;
import static pl.boringstuff.calculator.project.ReportFormat.CSV;
import pl.boringstuff.calculator.raport.ContributionReport;
import pl.boringstuff.calculator.stats.ProjectStats;
import pl.boringstuff.calculator.stats.UserContributionStats;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvWriter implements ReportWriter {
  private static final String PROJECT_CONTRIBUTION_STATS = "PROJECT_CONTRIBUTION_STATS";
  private static final String USER_CONTRIBUTION_RANGING = "USER_CONTRIBUTION_RANGING";
  private static final String EMPTY = "";
  private static final String ALL_PROJECTS_LABEL = "ALL_PROJECTS";
  private static final String DISTRIBUTION_STATS_PATTERN = "%s / %s / %s";
  private static final String REPORTS_DIR = "reports";
  private static final String[] HEADERS = {"report_part_name", "project_name", "excluded_paths", "user_name", "loc", "commits", "files", "distribution"};

  @Override
  public void write(final ContributionReport report) {
    try (BufferedWriter writer = Files.newBufferedWriter(createReportFile(report));
         CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))) {
      printTotalUserContribution(csvPrinter, report);
      printProjectContribution(csvPrinter, report);
      csvPrinter.flush();
    } catch (IOException ex) {
      throw new ReportException(ex.getMessage(), CSV);
    }
  }

  private void printTotalUserContribution(final CSVPrinter csvPrinter, final ContributionReport report) throws IOException {
    for (UserContributionStats userStats : report.totalUserContributionStats()) {
      csvPrinter.printRecord(USER_CONTRIBUTION_RANGING, ALL_PROJECTS_LABEL, EMPTY,
              userStats.user().name(),
              userStats.counts().lineOfCode(), userStats.counts().commits(), userStats.counts().files(),
              EMPTY);
    }
  }

  private void printProjectContribution(final CSVPrinter csvPrinter, final ContributionReport report) throws IOException {
    for (ProjectStats projectStats : report.projectContributionStats()) {
      for (UserContributionStats userStats : projectStats.userStats()) {
        final var project = projectStats.project();
        csvPrinter.printRecord(PROJECT_CONTRIBUTION_STATS,
                project.getName(), getExcludePaths(project),
                userStats.user().name(),
                userStats.counts().lineOfCode(), userStats.counts().commits(), userStats.counts().files(),
                projectDistribution(userStats));
      }
    }
  }

  private String projectDistribution(final UserContributionStats userStats) {
    final var dist = userStats.distribution();
    return DISTRIBUTION_STATS_PATTERN.formatted(dist.codeLinesParticipation(), dist.commitsParticipation(), dist.filesParticipation());
  }

  private String getExcludePaths(final Project project) {
    return String.join("/", project.getExcludePaths());
  }

  private Path createReportFile(final ContributionReport report) {
    return Path.of("%s/contribution-report-%s.csv".formatted(REPORTS_DIR, report.calculationDate()));
  }
}
