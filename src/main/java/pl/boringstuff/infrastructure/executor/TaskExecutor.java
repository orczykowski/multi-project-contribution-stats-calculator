package pl.boringstuff.infrastructure.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
  private static final int POOL_SIZE = 15;
  private static final int POOLE_QUEUE_SIZE_FACTOR = 4;

  private final ThreadPoolExecutor threadPoolExecutor;

  private TaskExecutor() {
    this.threadPoolExecutor = create();
  }

  public static TaskExecutor getInstance() {
    return HOLDER.TASK_EXECUTOR.executor;
  }

  public <T> CompletableFuture<T> sendToProcess(final Task<T> task) {
    return CompletableFuture.supplyAsync(task::run, threadPoolExecutor);
  }

  public void terminate() {
    threadPoolExecutor.shutdown();
  }

  private ThreadPoolExecutor create() {
    return new ThreadPoolExecutor(
            POOL_SIZE, POOL_SIZE, 0,
            TimeUnit.MILLISECONDS, queue(), rejectExecutingHandler());
  }

  private ArrayBlockingQueue<Runnable> queue() {
    return new ArrayBlockingQueue<>(POOL_SIZE * POOLE_QUEUE_SIZE_FACTOR);
  }

  private RejectedExecutionHandler rejectExecutingHandler() {
    return TaskExecutor::rejectedExecution;
  }

  private static void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    System.out.println("err");
  }

  enum HOLDER {
    TASK_EXECUTOR(new TaskExecutor());
    private final TaskExecutor executor;

    HOLDER(final TaskExecutor executor) {
      this.executor = executor;
    }
  }
}
