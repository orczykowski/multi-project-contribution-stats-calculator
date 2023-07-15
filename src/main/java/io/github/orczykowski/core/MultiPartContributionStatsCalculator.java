package io.github.orczykowski.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.github.orczykowski.AppRunner;
import io.github.orczykowski.adapter.writer.ReportWriterProvider;

import java.io.File;

@Component
public class MultiPartContributionStatsCalculator {

  private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

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
    log.info("starting verify necessary dictionaries");
    prepareAndVerifyNecessaryDirectories();
    log.info("starting calculate stats");
    final var calculationResult = calculator.calculate();
    final var reportWriter = reportWriterProvider.provideFor(specificationSupplier.supply().reportFormat());
    log.info("starting write report");
    reportWriter.write(calculationResult);
  }

  private void prepareAndVerifyNecessaryDirectories() {
    final var spec = specificationSupplier.supply();
    log.debug("verify working dir");
    safeCreateDir(spec.workingDir());
    log.debug("verify result dir");
    safeCreateDir(spec.resultDir());
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
