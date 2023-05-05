package pl.orczykowski.adapter.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.orczykowski.core.CalculationSpecificationSupplier;
import pl.orczykowski.core.FetchingProjectException;
import pl.orczykowski.core.project.Project;
import pl.orczykowski.core.project.ProjectRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class JsonFileProjectRepository implements ProjectRepository {

  private static final Logger log = LoggerFactory.getLogger(JsonFileProjectRepository.class);

  private final CalculationSpecificationSupplier calculationSpecificationSupplier;
  private final ObjectMapper objectMapper;

  private final Set<Project> projects;

  private JsonFileProjectRepository(
          final CalculationSpecificationSupplier calculationSpecificationSupplier,
          final ObjectMapper objectMapper) {
    this.calculationSpecificationSupplier = calculationSpecificationSupplier;
    this.objectMapper = objectMapper;
    this.projects = loadProjectsFromFile();
  }

  @Override
  public Stream<Project> findAll() {
    return projects.stream();
  }

  @Override
  public int count() {
    return projects.size();
  }

  private Set<Project> loadProjectsFromFile() {
    final var pathToProjectsRepositoriesFile = calculationSpecificationSupplier.supply()
            .projectsRepositoryFilePath();
    try {
      return objectMapper.readValue(pathToProjectsRepositoriesFile.toFile(), ProjectsDto.class)
              .projects()
              .stream()
              .filter(Objects::nonNull)
              .map(this::asDomain)
              .collect(Collectors.toUnmodifiableSet());
    } catch (final IOException ex) {
      log.error("Cannot parse json file repository [{}]", ex.getMessage(), ex);
      throw new FetchingProjectException(pathToProjectsRepositoriesFile, ex);
    }
  }

  private Project asDomain(final ProjectDto dto) {
    return new Project(dto.url(), dto.excludePaths());
  }

  record ProjectsDto(List<ProjectDto> projects) {}
  record ProjectDto(String url, Set<String> excludePaths) {}
}
