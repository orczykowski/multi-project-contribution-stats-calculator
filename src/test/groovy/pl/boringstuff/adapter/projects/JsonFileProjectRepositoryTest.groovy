package pl.boringstuff.adapter.projects

import com.fasterxml.jackson.databind.ObjectMapper
import pl.boringstuff.core.CalculationSpecificationSupplier
import pl.boringstuff.core.project.Project
import pl.boringstuff.core.project.ProjectRepository
import pl.boringstuff.core.project.ReportFormat
import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Collectors

class JsonFileProjectRepositoryTest extends Specification {

    @Subject
    ProjectRepository repository

    def setup() {
        def pathToRepo = new File("src/test/resources/projects.json").getAbsolutePath()
        repository = new JsonFileProjectRepository({
            new CalculationSpecificationSupplier.CalculationSpecification(null, null, pathToRepo, ReportFormat.HTML, "/tmp", 100)
        }, new ObjectMapper())
    }

    def "should return stream of all defined projects"() {
        when:
            def projects = repository.findAll().collect(Collectors.toList())

        then:
            projects.containsAll([new Project("git@github.com:orczykowski/multi-project-contribution-stats-calculator.git", Set.of("path_to_exclude")),
                                  new Project("git@github.com:orczykowski/boring-examples.git", Set.of())])
    }

    def "should return number of unique repositories"() {
        expect:
            repository.count() == 2
    }
}
