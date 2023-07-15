package io.github.orczykowski.infrastructure.executor;

import io.github.orczykowski.core.CalculatorTaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ThreadPoolTaskExecutor implements CalculatorTaskProcessor {

  private static final Logger log = LoggerFactory.getLogger(ThreadPoolTaskExecutor.class);

  private final ExecutorConfiguration executorConfiguration;

  private final ThreadPoolExecutor threadPoolExecutor;

  private ThreadPoolTaskExecutor(final ExecutorConfiguration executorConfiguration) {
    log.debug("creating thread executor from {}", executorConfiguration);
    this.executorConfiguration = executorConfiguration;
    this.threadPoolExecutor = create();
  }

  public <T> CompletableFuture<T> sendToProcess(final Task<T> task) {
    return CompletableFuture.supplyAsync(task::run, threadPoolExecutor);
  }

  public void terminate() {
    threadPoolExecutor.shutdown();
  }

  private ThreadPoolExecutor create() {
    return new ThreadPoolExecutor(
            executorConfiguration.numberOfThreads(),
            executorConfiguration.numberOfThreads(), 0,
            TimeUnit.MILLISECONDS, queue(), rejectExecutingHandler());
  }

  private ArrayBlockingQueue<Runnable> queue() {
    return new ArrayBlockingQueue<>(executorConfiguration.queueSize());
  }

  private RejectedExecutionHandler rejectExecutingHandler() {
    return ThreadPoolTaskExecutor::rejectedExecution;
  }

  private static void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    log.error("task rejected");
  }
}
