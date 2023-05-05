package pl.orczykowski.infrastructure.executor;

public interface Task<T> {

  T run();
}
