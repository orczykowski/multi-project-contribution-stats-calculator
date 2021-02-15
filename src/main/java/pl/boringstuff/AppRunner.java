package pl.boringstuff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import pl.boringstuff.domain.MultiPartContributionStatsCalculator;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AppRunner {

  public static void main(String[] args) {
    new SpringApplication(AppRunner.class)
            .run(args)
            .getBean(MultiPartContributionStatsCalculator.class)
            .createContributionReport();
    System.exit(0);
  }
}
