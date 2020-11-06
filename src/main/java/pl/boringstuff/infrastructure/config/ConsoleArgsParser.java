package pl.boringstuff.infrastructure.config;

import pl.boringstuff.calculator.project.ReportFormat;
import static pl.boringstuff.infrastructure.config.AppDefaultsConfig.DEFAULT_DATE_FROM;
import static pl.boringstuff.infrastructure.config.AppDefaultsConfig.DEFAULT_PROJECT_REPOSITORY_PATH;
import static pl.boringstuff.infrastructure.config.AppDefaultsConfig.DEFAULT_REPORT_FORMAT;
import static pl.boringstuff.infrastructure.config.AppDefaultsConfig.DEFAULT_RESULT_DIR;
import static pl.boringstuff.infrastructure.config.AppDefaultsConfig.DEFAULT_WORKING_DIR;
import pl.boringstuff.infrastructure.utils.Pair;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ConsoleArgsParser {
  private static final String DATE_FROM = "dateFrom";
  private static final String RESULT_PATH = "resultDir";
  private static final String REPO_PATH = "repoPath";
  private static final String REPORT_FORMAT = "reportFormat";
  private static final String WORKING_DIT = "workingDir";

  private static final Pattern ARG_FORMAT_PATTERN = Pattern.compile("^(dateFrom|resultDir|repoPath|reportFormat|workingDir)=.*");

  private ConsoleArgsParser() {
  }

  static ConsoleArgsParser getInstance() {
    return HOLDER.INSTANCE.instance;
  }

  public CalculationParameters parse(final String[] args) {
    var rawArgsMap = Arrays.stream(args)
            .map(String::trim)
            .filter(this::checkFormat)
            .map(this::asNameValuePair)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Pair::first, Pair::second));

    return new CalculationParameters(startCalculationDate(rawArgsMap),
            resultDir(rawArgsMap),
            repoPath(rawArgsMap),
            reportFormat(rawArgsMap),
            workingDir(rawArgsMap));
  }

  private String resultDir(final Map<String, String> rawArgsMap) {
    return rawArgsMap.getOrDefault(RESULT_PATH, DEFAULT_RESULT_DIR);
  }

  private String workingDir(final Map<String, String> rawArgsMap) {
    return rawArgsMap.getOrDefault(WORKING_DIT, DEFAULT_WORKING_DIR);
  }

  private String repoPath(final Map<String, String> rawArgsMap) {
    return rawArgsMap.getOrDefault(REPO_PATH, DEFAULT_PROJECT_REPOSITORY_PATH);
  }

  private ReportFormat reportFormat(final Map<String, String> rawArgsMap) {
    return Optional.ofNullable(rawArgsMap.get(REPORT_FORMAT))
            .map(String::toUpperCase)
            .map(ReportFormat::valueOf)
            .orElse(DEFAULT_REPORT_FORMAT);
  }

  private LocalDate startCalculationDate(final Map<String, String> rawArgsMap) {
    return Optional.ofNullable(rawArgsMap.get(DATE_FROM))
            .map(LocalDate::parse)
            .orElse(DEFAULT_DATE_FROM);
  }

  private Pair<String, String> asNameValuePair(final String arg) {
    final var splited = arg.split("=");
    if (splited.length != 2) {
      return null;
    }
    return Pair.of(splited[0], splited[1]);
  }

  private boolean checkFormat(final String arg) {
    return ARG_FORMAT_PATTERN.matcher(arg).matches();
  }

  private enum HOLDER {
    INSTANCE(new ConsoleArgsParser());
    private final ConsoleArgsParser instance;

    HOLDER(final ConsoleArgsParser instance) {
      this.instance = instance;
    }
  }
}
