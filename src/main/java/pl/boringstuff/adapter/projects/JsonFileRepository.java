package pl.boringstuff.adapter.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.boringstuff.domain.CalculationSpecificationSupplier;
import pl.boringstuff.domain.FetchingProjectException;
import pl.boringstuff.domain.project.Project;
import pl.boringstuff.domain.project.ProjectRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Component
class JsonFileRepository implements ProjectRepository {
  private final CalculationSpecificationSupplier calculationSpecificationSupplier;
  private final ObjectMapper objectMapper;

  private JsonFileRepository(final CalculationSpecificationSupplier calculationSpecificationSupplier, final ObjectMapper objectMapper) {
    this.calculationSpecificationSupplier = calculationSpecificationSupplier;
    this.objectMapper = objectMapper;
  }

  @Override
  public Stream<Project> findAll() {
    final var pathToProjectsRepositoriesFile = calculationSpecificationSupplier.supply().projectsRepositoryFilePath();
    try {
      return objectMapper.readValue(pathToProjectsRepositoriesFile.toFile(), ProjectsDto.class)
              .projects()
              .stream()
              .filter(Objects::nonNull)
              .map(this::asDomain);
    } catch (final IOException ex) {
      throw new FetchingProjectException(pathToProjectsRepositoriesFile, ex);
    }
  }

  private Project asDomain(final ProjectDto dto) {
    return new Project(dto.url(), dto.excludePaths());
  }

  record ProjectsDto(List<ProjectDto> projects) {
  }

  record ProjectDto(String url, Set<String> excludePaths) {
  }
}
