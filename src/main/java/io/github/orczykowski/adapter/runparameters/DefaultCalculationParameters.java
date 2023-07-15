package io.github.orczykowski.adapter.runparameters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.orczykowski.core.project.ReportFormat;

@ConfigurationProperties(prefix = "run")
record DefaultCalculationParameters(String dateFrom,
                                    String resultDir,
                                    String repoPath,
                                    ReportFormat reportFormat,
                                    String workingDir,
                                    Long timoutInSeconds) {

}
