package io.github.orczykowski.core;

import io.github.orczykowski.infrastructure.executor.Task;

import java.util.concurrent.CompletableFuture;

public interface CalculatorTaskProcessor {

    <T> CompletableFuture<T> sendToProcess(final Task<T> task);
}
