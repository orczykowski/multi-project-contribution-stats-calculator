package pl.boringstuff.core;

import pl.boringstuff.core.project.ReportFormat;

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
        private final String resultDir;

        public CalculationSpecification(final LocalDate dateFrom, final String resultDir, final String repoPath, final ReportFormat reportFormat, final String workingDir, final Long timoutInSeconds) {
            this.dateFrom = dateFrom;
            this.repoPath = Path.of(repoPath);
            this.reportFormat = reportFormat;
            this.workingDir = workingDir;
            this.timoutInSeconds = timoutInSeconds;
            this.resultDir = resultDir;
            this.resultPathPattern = resultPathPattern();
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

        public String resultDir() {
            return resultDir;
        }

        private String resultPathPattern() {
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
