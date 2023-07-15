package io.github.orczykowski.core.stats.calculator.jgit;

import io.github.orczykowski.core.stats.model.ContributionStats;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

class JGitDiffStatsCalculator {

    private static final Logger log = LoggerFactory.getLogger(JGitDiffStatsCalculator.class);

    private static final Set<String> CONFIG_EXTENSIONS = Set.of(
            ".yaml", ".yml", ".properties", ".xml", ".json", ".toml",
            ".ini", ".cfg", ".conf", ".env"
    );

    private static final Set<String> CONFIG_FILENAMES = Set.of(
            "dockerfile", "makefile", "build.gradle", "pom.xml",
            "settings.gradle", "gradle.properties"
    );

    private final Repository repository;

    JGitDiffStatsCalculator(final Repository repository) {
        this.repository = repository;
    }

    Map<String, ContributionStats> calculate(final LocalDate dateFrom,
                                             final LocalDate dateTo,
                                             final Set<String> excludePaths) {
        final Map<String, Long> commitsByAuthor = new HashMap<>();
        final Map<String, Long> addedByAuthor = new HashMap<>();
        final Map<String, Long> removedByAuthor = new HashMap<>();
        final Map<String, Long> filesChangedByAuthor = new HashMap<>();
        final Map<String, Long> newFilesByAuthor = new HashMap<>();
        final Map<String, Long> productionLinesByAuthor = new HashMap<>();
        final Map<String, Long> testLinesByAuthor = new HashMap<>();
        final Map<String, Long> configLinesByAuthor = new HashMap<>();

        try (RevWalk walk = new RevWalk(repository);
             DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {

            diffFormatter.setRepository(repository);
            diffFormatter.setDetectRenames(true);

            ObjectId head = repository.resolve("HEAD");
            if (head == null) {
                log.warn("Repository has no HEAD");
                return Collections.emptyMap();
            }
            walk.markStart(walk.parseCommit(head));

            for (RevCommit commit : walk) {
                LocalDate commitDate = Instant.ofEpochSecond(commit.getCommitTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (commitDate.isBefore(dateFrom)) {
                    continue;
                }
                if (dateTo != null && commitDate.isAfter(dateTo)) {
                    continue;
                }

                String author = commit.getAuthorIdent().getName();

                AbstractTreeIterator parentIterator;
                if (commit.getParentCount() > 0) {
                    RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
                    parentIterator = prepareTreeParser(parent);
                } else {
                    parentIterator = new EmptyTreeIterator();
                }

                AbstractTreeIterator commitIterator = prepareTreeParser(commit);
                List<DiffEntry> diffs = diffFormatter.scan(parentIterator, commitIterator);

                long linesAdded = 0;
                long linesRemoved = 0;
                long filesChanged = 0;
                long newFiles = 0;
                long productionLines = 0;
                long testLines = 0;
                long configLines = 0;

                for (DiffEntry diff : diffs) {
                    String path = diff.getChangeType() == DiffEntry.ChangeType.DELETE
                            ? diff.getOldPath()
                            : diff.getNewPath();

                    if (isExcluded(path, excludePaths)) {
                        continue;
                    }

                    filesChanged++;

                    if (diff.getChangeType() == DiffEntry.ChangeType.ADD) {
                        newFiles++;
                    }

                    long fileAdded = 0;
                    for (Edit edit : diffFormatter.toFileHeader(diff).toEditList()) {
                        long added = edit.getEndB() - edit.getBeginB();
                        long removed = edit.getEndA() - edit.getBeginA();
                        linesAdded += added;
                        linesRemoved += removed;
                        fileAdded += added;
                    }

                    if (fileAdded > 0) {
                        if (isTestFile(path)) {
                            testLines += fileAdded;
                        } else if (isConfigFile(path)) {
                            configLines += fileAdded;
                        } else {
                            productionLines += fileAdded;
                        }
                    }
                }

                commitsByAuthor.merge(author, 1L, Long::sum);
                addedByAuthor.merge(author, linesAdded, Long::sum);
                removedByAuthor.merge(author, linesRemoved, Long::sum);
                filesChangedByAuthor.merge(author, filesChanged, Long::sum);
                newFilesByAuthor.merge(author, newFiles, Long::sum);
                productionLinesByAuthor.merge(author, productionLines, Long::sum);
                testLinesByAuthor.merge(author, testLines, Long::sum);
                configLinesByAuthor.merge(author, configLines, Long::sum);
            }
        } catch (IOException ex) {
            log.error("Failed to walk repository commits", ex);
            return Collections.emptyMap();
        }

        Map<String, ContributionStats> result = new HashMap<>();
        for (String author : commitsByAuthor.keySet()) {
            result.put(author, new ContributionStats(
                    commitsByAuthor.getOrDefault(author, 0L),
                    addedByAuthor.getOrDefault(author, 0L),
                    removedByAuthor.getOrDefault(author, 0L),
                    filesChangedByAuthor.getOrDefault(author, 0L),
                    newFilesByAuthor.getOrDefault(author, 0L),
                    productionLinesByAuthor.getOrDefault(author, 0L),
                    testLinesByAuthor.getOrDefault(author, 0L),
                    configLinesByAuthor.getOrDefault(author, 0L)));
        }
        return result;
    }

    private boolean isTestFile(final String path) {
        return path.contains("/test/");
    }

    private boolean isConfigFile(final String path) {
        String lowerPath = path.toLowerCase();
        String fileName = lowerPath.contains("/")
                ? lowerPath.substring(lowerPath.lastIndexOf('/') + 1)
                : lowerPath;

        if (CONFIG_FILENAMES.contains(fileName) || fileName.startsWith("docker-compose")) {
            return true;
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            String ext = fileName.substring(dotIndex);
            return CONFIG_EXTENSIONS.contains(ext);
        }
        return false;
    }

    private boolean isExcluded(final String path, final Set<String> excludePaths) {
        if (excludePaths == null || excludePaths.isEmpty()) {
            return false;
        }
        return excludePaths.stream().anyMatch(path::startsWith);
    }

    private CanonicalTreeParser prepareTreeParser(final RevCommit commit) throws IOException {
        try (var reader = repository.newObjectReader()) {
            var treeParser = new CanonicalTreeParser();
            treeParser.reset(reader, commit.getTree().getId());
            return treeParser;
        }
    }
}
