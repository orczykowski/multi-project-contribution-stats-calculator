package io.github.orczykowski;

import io.github.orczykowski.core.MultiPartContributionStatsCalculator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

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
