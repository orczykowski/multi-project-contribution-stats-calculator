package pl.boringstuff.infrastructure;

public enum APP_DEFAULTS {
  DATE_FROM("2020-01-01"),
  RESULT_DIR("reports"),
  REPO_PATH("projects.json"),
  REPORT_FORMAT("HTML");

  private final String value;

  private APP_DEFAULTS(final String value) {
    this.value = value;
  }

  String getValue() {
    return value;
  }
}
