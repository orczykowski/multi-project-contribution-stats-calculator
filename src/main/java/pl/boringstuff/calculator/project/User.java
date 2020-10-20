package pl.boringstuff.calculator.project;

import java.util.Objects;

public record User(String name) {
  private static final String UNKNOWN_USER = "<unknown user>";

  public User(final String name) {
    final var normalizedName = Objects.requireNonNullElse(name.trim(), UNKNOWN_USER);
    this.name = normalizedName;
  }
}
