package pl.boringstuff.domain;

import pl.boringstuff.infrastructure.executor.Task;

import java.util.concurrent.CompletableFuture;

public interface CalculatorTaskProcessor {
  <T> CompletableFuture<T> sendToProcess(final Task<T> task);
}
