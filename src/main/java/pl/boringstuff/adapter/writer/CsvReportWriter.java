package pl.boringstuff.adapter.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.CalculationSpecificationSupplier;
import pl.boringstuff.core.project.Project;
import pl.boringstuff.core.project.ReportFormat;
import pl.boringstuff.core.raport.ContributionReport;
import pl.boringstuff.core.raport.ReportWriter;
import pl.boringstuff.core.stats.model.ProjectStats;
import pl.boringstuff.core.stats.model.UserContributionStats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static pl.boringstuff.core.project.ReportFormat.CSV;

@Component
public class CsvReportWriter implements ReportWriter {

    private static final String PROJECT_CONTRIBUTION_STATS = "PROJECT_CONTRIBUTION_STATS";
    private static final String USER_CONTRIBUTION_RANGING = "USER_CONTRIBUTION_RANGING";
    private static final String EMPTY = "";
    private static final String ALL_PROJECTS_LABEL = "ALL_PROJECTS";
    private static final String DISTRIBUTION_STATS_PATTERN = "%s / %s / %s";
    private static final String[] HEADERS = {"report_part_name", "project_name", "excluded_paths",
            "user_name", "loc", "commits", "files", "distribution"};

    private final CalculationSpecificationSupplier specificationSupplier;

    CsvReportWriter(final CalculationSpecificationSupplier specificationSupplier) {
        this.specificationSupplier = specificationSupplier;
    }

    @Override
    public void write(final ContributionReport report) {
        try (final var writer = Files.newBufferedWriter(createReportPath(report));
             final var csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            printTotalUserContribution(csvPrinter, report);
            printProjectContribution(csvPrinter, report);
            csvPrinter.flush();
        } catch (IOException ex) {
            throw new ReportException(ex.getMessage(), CSV);
        }
    }

    @Override
    public ReportFormat format() {
        return CSV;
    }

    private Path createReportPath(final ContributionReport report) {
        return specificationSupplier.supply().reportPath(report.calculationDate());
    }

    private void printTotalUserContribution(final CSVPrinter csvPrinter,
                                            final ContributionReport report) throws IOException {
        for (UserContributionStats userStats : report.totalUserContributionStats()) {
            csvPrinter.printRecord(USER_CONTRIBUTION_RANGING, ALL_PROJECTS_LABEL, EMPTY,
                    userStats.user().name(),
                    userStats.counts().lineOfCode(), userStats.counts().commits(), userStats.counts().files(),
                    EMPTY);
        }
    }

    private void printProjectContribution(final CSVPrinter csvPrinter,
                                          final ContributionReport report) throws IOException {
        for (ProjectStats projectStats : report.projectContributionStats()) {
            for (UserContributionStats userStats : projectStats.userStats()) {
                final var project = projectStats.project();
                csvPrinter.printRecord(PROJECT_CONTRIBUTION_STATS,
                        project.getName(), getExcludePaths(project),
                        userStats.user().name(),
                        userStats.counts().lineOfCode(), userStats.counts().commits(),
                        userStats.counts().files(),
                        projectDistribution(userStats));
            }
        }
    }

    private String projectDistribution(final UserContributionStats userStats) {
        final var dist = userStats.distribution();
        return DISTRIBUTION_STATS_PATTERN.formatted(dist.codeLinesParticipation(),
                dist.commitsParticipation(), dist.filesParticipation());
    }

    private String getExcludePaths(final Project project) {
        return project.getJoinedExcludedPaths("/");
    }

}
