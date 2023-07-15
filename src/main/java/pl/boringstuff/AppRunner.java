package pl.boringstuff;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import pl.boringstuff.core.MultiPartContributionStatsCalculator;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AppRunner {

  public static void main(final String[] args) {
    new SpringApplicationBuilder(AppRunner.class)
        .run(args)
        .getBean(MultiPartContributionStatsCalculator.class)
        .createContributionReport();
    System.exit(0);
  }
}
