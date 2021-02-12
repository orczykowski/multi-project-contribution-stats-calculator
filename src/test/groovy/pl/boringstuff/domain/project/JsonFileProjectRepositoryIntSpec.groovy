package pl.boringstuff.domain.project

import spock.lang.Specification

import java.util.stream.Collectors


class JsonFileProjectRepositoryIntSpec extends Specification {
/*
  def setup() {
    CalculationParamsProvider.init(["repoPath=src/test/resources/projects.json"] as String[])
  }

  def "should return stream of all defined projects"() {
    given:
      def repository = JsonFileProjectRepository.getInstance()

    when:
      def projects = repository.findAll()

    then:
      projects != null

    and:
      def projectsList = projects.collect(Collectors.toList())
      projectsList.size() == 1
    and:
      def project = projectsList.first()
      project != null
      project.name == "multi-project-contribution-stats-calculator"
      project.repositoryUrl == "git@github.com:orczykowski/multi-project-contribution-stats-calculator.git"
      project.excludePaths == ["package-lock.json"] as Set

  }*/
}
