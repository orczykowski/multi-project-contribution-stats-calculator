package pl.boringstuff.domain.project;

import java.util.stream.Stream;

public interface ProjectRepository {
  Stream<Project> findAll();
}