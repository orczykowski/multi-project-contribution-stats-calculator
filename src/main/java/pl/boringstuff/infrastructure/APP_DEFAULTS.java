package pl.boringstuff.infrastructure;

public enum APP_DEFAULTS {
  DATE_FROM("1970-01-01"),
  RESULT_DIR("reports"),
  REPO_PATH("projects.json"),
  REPORT_FORMAT("HTML");

  private final String value;

  APP_DEFAULTS(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
