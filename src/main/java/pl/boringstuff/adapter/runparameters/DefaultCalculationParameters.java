package pl.boringstuff.adapter.runparameters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import pl.boringstuff.core.project.ReportFormat;

@ConstructorBinding
@ConfigurationProperties(prefix = "run")
record DefaultCalculationParameters(String dateFrom,
                                    String resultDir,
                                    String repoPath,
                                    ReportFormat reportFormat,
                                    String workingDir,
                                    Long timoutInSeconds) {

}
