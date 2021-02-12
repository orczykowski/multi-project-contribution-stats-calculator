package pl.boringstuff.domain;

import pl.boringstuff.domain.project.ReportFormat;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.StringJoiner;

public interface CalculationSpecificationSupplier {
  CalculationSpecification supply();

  class CalculationSpecification {
    private final LocalDate dateFrom;
    private final String resultPathPattern;
    private final Path repoPath;
    private final ReportFormat reportFormat;
    private final String workingDir;
    private final Long timoutInSeconds;

    public CalculationSpecification(final LocalDate dateFrom, final String resultDir, final String repoPath,
                                    final ReportFormat reportFormat, final String workingDir, final Long timoutInSeconds) {
      this.dateFrom = dateFrom;
      this.resultPathPattern = resultPathPattern(resultDir, reportFormat);
      this.repoPath = Path.of(repoPath);
      this.reportFormat = reportFormat;
      this.workingDir = workingDir;
      this.timoutInSeconds = timoutInSeconds;
    }

    public LocalDate dateFrom() {
      return this.dateFrom;
    }

    public Path reportPath(final Instant calculationTime) {
      return Path.of(resultPathPattern.formatted(calculationTime));
    }

    public Path projectsRepositoryFilePath() {
      return this.repoPath;
    }

    public ReportFormat reportFormat() {
      return this.reportFormat;
    }

    public String workingDir() {
      return this.workingDir;
    }

    public Long timoutInSeconds() {
      return this.timoutInSeconds;
    }

    private String resultPathPattern(final String resultDir, final ReportFormat reportFormat) {
      return resultDir + "/contribution-report-%s." + reportFormat.toString().toLowerCase();
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", CalculationSpecification.class.getSimpleName() + "[", "]")
              .add("dateFrom=" + dateFrom)
              .add("resultPathPattern='" + resultPathPattern + "'")
              .add("repoPath='" + repoPath + "'")
              .add("reportFormat=" + reportFormat)
              .add("workingDir='" + workingDir + "'")
              .add("timoutInSeconds=" + timoutInSeconds)
              .toString();
    }
  }
}
