package pl.boringstuff.core.stats.calculator;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.project.User;
import pl.boringstuff.core.stats.model.ContributionStats;
import pl.boringstuff.core.stats.model.ProjectDistributionStats;
import pl.boringstuff.core.stats.model.UserContributionStats;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class GitFameRawCsvStatsResultParser {
  private static final String NAME = "name";
  private static final String LOC = "loc";
  private static final String COMMITS = "commits";
  private static final String FILES = "files";
  private static final String DISTRIBUTION = "distribution";

  private static final String SPLIT_DISTRIBUTION_SYMBOL = "/";

  private static final Pattern LONG_PATTERN = Pattern.compile("[0-9]+");
  private static final Pattern NUMBER_PATTER = Pattern.compile("[0-9]+\\.[0-9]+");

  private static final String[] HEADERS;
  private static final CSVFormat CSV_FORMAT;

  static {
    HEADERS = new String[]{NAME, LOC, COMMITS, FILES, DISTRIBUTION};
    CSV_FORMAT = CSVFormat.DEFAULT
            .withHeader(HEADERS)
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase();
  }

  private GitFameRawCsvStatsResultParser() {
  }

  public List<UserContributionStats> parse(final RawStatsCalculationResult.Success rawResult) {
    if (!validate(rawResult)) {
      return List.of();
    }
    try {
      return CSV_FORMAT.parse(new StringReader(rawResult.rawStats())).getRecords()
              .stream()
              .map(this::asUserStats)
              .collect(Collectors.toUnmodifiableList());
    } catch (IOException e) {
      throw new ParseResultException(e.getMessage());
    }
  }

  private UserContributionStats asUserStats(final CSVRecord line) {
    var user = new User(line.get(NAME));
    var contributionStats = contributionStats(line);
    var distributionStats = distributionStats(line);
    return new UserContributionStats(user, contributionStats, distributionStats);
  }

  private ProjectDistributionStats distributionStats(final CSVRecord line) {

    var distribution = Arrays.asList(line.get(DISTRIBUTION).split(SPLIT_DISTRIBUTION_SYMBOL));

    if (distribution.size() != 3) {
      return ProjectDistributionStats.empty();
    }
    var distributionLines = parseDistributionPart(distribution, 0);
    var distributionCommits = parseDistributionPart(distribution, 1);
    var distributionFiles = parseDistributionPart(distribution, 2);
    return new ProjectDistributionStats(distributionLines, distributionCommits, distributionFiles);
  }

  private BigDecimal parseDistributionPart(final List<String> dist, int idx) {
    return Optional.ofNullable(dist.get(idx))
            .map(String::trim)
            .filter(this::isNumber)
            .map(BigDecimal::new)
            .orElse(BigDecimal.ZERO);
  }

  private boolean validate(final RawStatsCalculationResult.Success result) {
    final var rawResult = result.rawStats();
    return Objects.nonNull(rawResult) && !rawResult.isBlank();
  }

  private boolean isNumber(final String it) {
    return NUMBER_PATTER.matcher(it).find();
  }

  private ContributionStats contributionStats(final CSVRecord line) {
    return new ContributionStats(
            parseStat(line, LOC),
            parseStat(line, COMMITS),
            parseStat(line, FILES)
    );
  }

  private Long parseStat(final CSVRecord line, final String columnName) {
    return Optional.ofNullable(line.get(columnName))
            .filter(this::isLong)
            .map(it -> it.replaceAll("[^\\d.]", ""))
            .map(Long::parseLong)
            .orElse(0L);
  }

  private boolean isLong(final String it) {
    return LONG_PATTERN.matcher(it).find();
  }
}
