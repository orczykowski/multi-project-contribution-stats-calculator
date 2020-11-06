package pl.boringstuff.calculator.project;

import com.google.gson.Gson;
import static pl.boringstuff.infrastructure.config.CalculationParamsProvider.getCalculationParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class JsonFileProjectRepository implements ProjectRepository {

  private JsonFileProjectRepository() {
  }

  public static ProjectRepository getInstance() {
    return HOLDER.PROJECT_JSON_REPOSITORY.INSTANCE;
  }

  @Override
  public Stream<Project> findAll() {
    try {
      return new Gson().fromJson(readProjectDefinitionsFile(), ProjectsEntity.class)
              .projects.stream()
              .filter(Objects::nonNull)
              .map(entity -> new Project(entity.url, entity.excludePaths));
    } catch (IOException e) {
      throw new FetchingProjectException(e.getMessage());
    }
  }

  private BufferedReader readProjectDefinitionsFile() throws IOException {
    final var file = Path.of(getCalculationParams().repoPath());
    return Files.newBufferedReader(file);
  }

  static class ProjectsEntity {
    List<ProjectEntity> projects;
  }

  static class ProjectEntity {
    String url;
    Set<String> excludePaths;
  }

  enum HOLDER {
    PROJECT_JSON_REPOSITORY(new JsonFileProjectRepository());
    private final ProjectRepository INSTANCE;

    HOLDER(final ProjectRepository instance) {
      INSTANCE = instance;
    }
  }
}