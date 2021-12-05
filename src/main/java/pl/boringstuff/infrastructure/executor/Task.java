package pl.boringstuff.infrastructure.executor;

public interface Task<T> {

  T run();
}
