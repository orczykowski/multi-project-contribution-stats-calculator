package pl.boringstuff.core;

import org.springframework.stereotype.Component;
import pl.boringstuff.adapter.writer.ReportWriterProvider;

import java.io.File;

@Component
public class MultiPartContributionStatsCalculator {
  private final ContributionStatsCalculator calculator;
  private final ReportWriterProvider reportWriterProvider;
  private final CalculationSpecificationSupplier specificationSupplier;

  public MultiPartContributionStatsCalculator(final ContributionStatsCalculator calculator,
                                              final ReportWriterProvider reportWriterProvider,
                                              final CalculationSpecificationSupplier specificationSupplier) {
    this.calculator = calculator;
    this.reportWriterProvider = reportWriterProvider;
    this.specificationSupplier = specificationSupplier;
  }

  public void createContributionReport() {

    prepareAndVerifyNecessaryDirectories();
    final var calculationResult = calculator.calculate();
    final var reportWriter = reportWriterProvider.provideFor(specificationSupplier.supply().reportFormat());
    reportWriter.write(calculationResult);
  }

  private void prepareAndVerifyNecessaryDirectories() {
    final var spec = specificationSupplier.supply();
    safeCreateDir(spec.workingDir());
    if (!spec.projectsRepositoryFilePath().toFile().exists()) {
      throw new IllegalStateException("repository with project to analyze does not exists, [%s]".formatted(spec.projectsRepositoryFilePath()));
    }
  }

  void safeCreateDir(final String path) {
    final var file = new File(path);
    if (!(file.exists() && file.isDirectory())) {
      file.mkdir();
    }
  }
}
