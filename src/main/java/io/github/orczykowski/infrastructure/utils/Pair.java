package io.github.orczykowski.infrastructure.utils;

public record Pair<T, R>(T first, R second) {

  public static <T, R> Pair<T, R> of(final T first, final R second) {
    return new Pair<T, R>(first, second);
  }
}
