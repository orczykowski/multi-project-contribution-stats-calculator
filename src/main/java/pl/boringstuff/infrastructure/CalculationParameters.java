package pl.boringstuff.infrastructure;

import pl.boringstuff.calculator.project.ReportFormat;

import java.time.LocalDate;

public record CalculationParameters(LocalDate dateFrom,
                                    String resultDir,
                                    String repoPath,
                                    ReportFormat reportFormat) {
}
