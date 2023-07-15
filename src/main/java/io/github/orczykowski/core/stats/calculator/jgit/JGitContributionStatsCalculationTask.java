package io.github.orczykowski.core.stats.calculator.jgit;

import io.github.orczykowski.core.ProjectCalculationStatsResult;
import io.github.orczykowski.core.project.Project;
import io.github.orczykowski.core.project.User;
import io.github.orczykowski.core.stats.model.ProjectDistributionStats;
import io.github.orczykowski.core.stats.model.ProjectStats;
import io.github.orczykowski.core.stats.model.UserContributionStats;
import io.github.orczykowski.infrastructure.executor.Task;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static io.github.orczykowski.infrastructure.utils.Preconditions.checkRequiredArgument;

public class JGitContributionStatsCalculationTask implements Task<ProjectCalculationStatsResult> {

    private static final Logger log = LoggerFactory.getLogger(JGitContributionStatsCalculationTask.class);

    private final Project project;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final String workingDir;

    public JGitContributionStatsCalculationTask(final Project project,
                                                 final LocalDate dateFrom,
                                                 final LocalDate dateTo,
                                                 final String workingDir) {
        checkRequiredArgument(project);
        checkRequiredArgument(dateFrom);
        this.project = project;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.workingDir = workingDir;
    }

    @Override
    public ProjectCalculationStatsResult run() {
        final File repoDir = new File(workingDir, project.getName());
        try {
            log.info("Cloning {} into {}", project.getRepositoryUrl(), repoDir);
            try (Git git = Git.cloneRepository()
                    .setURI(project.getRepositoryUrl())
                    .setDirectory(repoDir)
                    .call()) {

                final var calculator = new JGitDiffStatsCalculator(git.getRepository());
                final var statsByAuthor = calculator.calculate(dateFrom, dateTo, project.getExcludePaths());

                final var userStats = statsByAuthor.entrySet().stream()
                        .map(entry -> new UserContributionStats(
                                new User(entry.getKey()),
                                entry.getValue(),
                                ProjectDistributionStats.empty()))
                        .collect(Collectors.toList());

                return new ProjectCalculationStatsResult.Success(new ProjectStats(project, userStats));
            }
        } catch (Exception ex) {
            log.error("Failed to calculate stats for {}", project.getName(), ex);
            return new ProjectCalculationStatsResult.Failure(project, ex.getMessage());
        } finally {
            cleanup(repoDir);
        }
    }

    private void cleanup(final File repoDir) {
        try {
            FileUtils.delete(repoDir, FileUtils.RECURSIVE | FileUtils.RETRY);
        } catch (IOException e) {
            log.error("Cannot remove repo dir {}", repoDir, e);
        }
    }
}
