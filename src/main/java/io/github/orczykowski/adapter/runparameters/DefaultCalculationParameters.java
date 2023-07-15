package io.github.orczykowski.adapter.runparameters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import io.github.orczykowski.core.project.ReportFormat;

@ConstructorBinding
@ConfigurationProperties(prefix = "run")
record DefaultCalculationParameters(String dateFrom,
                                    String resultDir,
                                    String repoPath,
                                    ReportFormat reportFormat,
                                    String workingDir,
                                    Long timoutInSeconds) {

}
