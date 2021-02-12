package pl.boringstuff.adapter.runparameters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "run-defaults")
        record CalculationParameters(String dateFrom,
                                     String resultDir,
                                     String repoPath,
                                     String reportFormat,
                                     String workingDir,
                                     Long timoutInSeconds) {
}
