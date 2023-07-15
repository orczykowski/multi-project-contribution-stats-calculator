package io.github.orczykowski.infrastructure.executor;

public interface Task<T> {

  T run();
}
