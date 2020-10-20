package pl.boringstuff.infrastructure;

import pl.boringstuff.calculator.project.ReportFormat;
import pl.boringstuff.infrastructure.utils.Pair;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsoleArgsParser {
  private static final String DATE_FROM = "dateFrom";
  private static final String RESULT_PATH = "resultDir";
  private static final String REPO_PATH = "repoPath";
  private static final String REPORT_FORMAT = "reportFormat";

  private static final Pattern ARG_FORMAT_PATTERN = Pattern.compile("^(dateFrom|resultDir|repoPath|reportFormat)=.*");

  public ConsoleArgsParser() {
  }

  public CalculationParameters parse(final String[] args) {
    var rawArgsMap = Arrays.stream(args)
            .map(String::trim)
            .filter(this::checkFormat)
            .map(this::asNameValuePair)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Pair::first, Pair::second));
    return new CalculationParameters(startCalculationDate(rawArgsMap), resultDir(rawArgsMap), repoPath(rawArgsMap), reportFormat(rawArgsMap));
  }

  private String resultDir(final Map<String, String> rawArgsMap) {
    return rawArgsMap.getOrDefault(RESULT_PATH, APP_DEFAULTS.RESULT_DIR.getValue());
  }

  private String repoPath(final Map<String, String> rawArgsMap) {
    return rawArgsMap.getOrDefault(REPO_PATH, APP_DEFAULTS.REPO_PATH.getValue());
  }

  private ReportFormat reportFormat(final Map<String, String> rawArgsMap) {
    final String name = rawArgsMap.getOrDefault(REPORT_FORMAT, APP_DEFAULTS.REPORT_FORMAT.getValue()).toUpperCase();
    return ReportFormat.valueOf(name);
  }

  private LocalDate startCalculationDate(final Map<String, String> rawArgsMap) {
    return LocalDate.parse(rawArgsMap.getOrDefault(DATE_FROM, APP_DEFAULTS.DATE_FROM.getValue()));
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


}
