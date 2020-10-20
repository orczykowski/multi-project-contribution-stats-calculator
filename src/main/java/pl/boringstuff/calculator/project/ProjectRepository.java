package pl.boringstuff.calculator.project;

import java.util.stream.Stream;

public interface ProjectRepository {
  Stream<Project> findAll();
}