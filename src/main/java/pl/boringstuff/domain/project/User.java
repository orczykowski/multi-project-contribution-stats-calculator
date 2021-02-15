package pl.boringstuff.domain.project;

public record User(String name) {
  private static final String UNKNOWN_USER = "<unknown user>";

  public User(final String name) {
    this.name = getNormalizedOrDefaultName(name);
  }

  private String getNormalizedOrDefaultName(String name) {
    if (name == null || name.isBlank()) {
      return UNKNOWN_USER;
    }
    return name.trim().toLowerCase();
  }
}
