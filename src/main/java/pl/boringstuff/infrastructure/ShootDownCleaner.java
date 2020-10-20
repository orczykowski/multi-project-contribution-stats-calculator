package pl.boringstuff.infrastructure;

import pl.boringstuff.infrastructure.command.Command;
import pl.boringstuff.infrastructure.executor.TaskExecutor;

import java.nio.file.Paths;
import java.util.List;

public class ShootDownCleaner {
  public static void cleanup() {
    TaskExecutor.getInstance().terminate();
    Command.newCommand("rm")
            .withArgs(List.of("-rf"))
            .inDictionary(Paths.get("").toAbsolutePath().toString().concat("/tmp_repos"))
            .build()
            .execute();
  }
}
