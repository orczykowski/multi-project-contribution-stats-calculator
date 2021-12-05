package pl.boringstuff.core.project;

import java.util.stream.Stream;

public interface ProjectRepository {

  Stream<Project> findAll();

  int count();
}
