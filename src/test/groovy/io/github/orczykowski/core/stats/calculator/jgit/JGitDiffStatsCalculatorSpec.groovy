package io.github.orczykowski.core.stats.calculator.jgit

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.PersonIdent
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.time.LocalDate

class JGitDiffStatsCalculatorSpec extends Specification {

    @TempDir
    File tempDir

    Git git

    def setup() {
        git = Git.init().setDirectory(tempDir).call()
    }

    def cleanup() {
        git?.close()
    }

    def "should count lines added for initial commit"() {
        given:
        def author = new PersonIdent("Alice", "alice@example.com")
        createFileAndCommit("file.txt", "line1\nline2\nline3\n", "initial commit", author)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        stats.size() == 1
        def alice = stats.get("Alice")
        alice != null
        alice.commits() == 1L
        alice.linesAdded() == 3L
        alice.linesRemoved() == 0L
        alice.filesChanged() == 1L
        alice.newFiles() == 1L
        alice.productionLinesAdded() == 3L
        alice.testLinesAdded() == 0L
        alice.configLinesAdded() == 0L
    }

    def "should count lines added and removed per author"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")
        def bob = new PersonIdent("Bob", "bob@example.com")

        createFileAndCommit("file.txt", "line1\nline2\nline3\nline4\nline5\n", "initial", alice)
        createFileAndCommit("file.txt", "line1\nmodified\nline3\nline4\nline5\nnew line\n", "modify", bob)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        stats.size() == 2

        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 1L
        aliceStats.linesAdded() == 5L
        aliceStats.linesRemoved() == 0L

        def bobStats = stats.get("Bob")
        bobStats.commits() == 1L
        bobStats.linesAdded() == 2L
        bobStats.linesRemoved() == 1L
    }

    def "should aggregate multiple commits by same author"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")

        createFileAndCommit("file1.txt", "line1\nline2\n", "first commit", alice)
        createFileAndCommit("file2.txt", "a\nb\nc\n", "second commit", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        stats.size() == 1
        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 2L
        aliceStats.linesAdded() == 5L
        aliceStats.linesRemoved() == 0L
    }

    def "should filter commits by date range - after only"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com", new Date(1577836800000), TimeZone.getTimeZone("UTC")) // 2020-01-01
        def bob = new PersonIdent("Bob", "bob@example.com") // current date

        createFileAndCommit("old.txt", "old content\n", "old commit", alice)
        createFileAndCommit("new.txt", "new content\n", "new commit", bob)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2025, 1, 1), null, Set.of())

        then:
        stats.size() == 1
        stats.get("Bob") != null
    }

    def "should exclude paths"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")

        createFileAndCommit("src/main.java", "code\n", "add main", alice)
        createFileAndCommit("docs/readme.txt", "docs\n", "add docs", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of("docs/"))

        then:
        stats.size() == 1
        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 2L
        aliceStats.linesAdded() == 1L
    }

    def "should handle file deletion"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")

        createFileAndCommit("file.txt", "line1\nline2\nline3\n", "add file", alice)
        deleteFileAndCommit("file.txt", "delete file", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        stats.size() == 1
        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 2L
        aliceStats.linesAdded() == 3L
        aliceStats.linesRemoved() == 3L
        aliceStats.filesChanged() == 2L
        aliceStats.newFiles() == 1L
    }

    def "should return empty map for empty repository"() {
        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        stats.isEmpty()
    }

    def "should return empty map when no commits in date range"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com", new Date(1577836800000), TimeZone.getTimeZone("UTC")) // 2020-01-01
        createFileAndCommit("file.txt", "content\n", "old commit", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2026, 1, 1), null, Set.of())

        then:
        stats.isEmpty()
    }

    def "should handle date range with dateTo"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com", new Date(1577836800000), TimeZone.getTimeZone("UTC")) // 2020-01-01
        def bob = new PersonIdent("Bob", "bob@example.com") // current date

        createFileAndCommit("old.txt", "old content\n", "old commit", alice)
        createFileAndCommit("new.txt", "new content\n", "new commit", bob)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), Set.of())

        then:
        stats.size() == 1
        stats.get("Alice") != null
    }

    def "should classify lines added in test files"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")
        createFileAndCommit("src/test/MyTest.java", "test line1\ntest line2\n", "add test", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.linesAdded() == 2L
        aliceStats.testLinesAdded() == 2L
        aliceStats.productionLinesAdded() == 0L
        aliceStats.configLinesAdded() == 0L
    }

    def "should classify lines added in config files"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")
        createFileAndCommit("application.yaml", "key: value\n", "add config", alice)
        createFileAndCommit("config.properties", "prop=val\n", "add props", alice)
        createFileAndCommit("Dockerfile", "FROM ubuntu\n", "add docker", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.linesAdded() == 3L
        aliceStats.configLinesAdded() == 3L
        aliceStats.productionLinesAdded() == 0L
        aliceStats.testLinesAdded() == 0L
    }

    def "should classify lines added in production files"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")
        createFileAndCommit("src/main/App.java", "code1\ncode2\ncode3\n", "add prod code", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.linesAdded() == 3L
        aliceStats.productionLinesAdded() == 3L
        aliceStats.testLinesAdded() == 0L
        aliceStats.configLinesAdded() == 0L
    }

    def "should classify mixed file types in same commit"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")

        // Create all files in one commit
        def prodFile = new File(tempDir, "src/main/App.java")
        Files.createDirectories(prodFile.getParentFile().toPath())
        prodFile.text = "prod1\nprod2\n"

        def testFile = new File(tempDir, "src/test/AppTest.java")
        Files.createDirectories(testFile.getParentFile().toPath())
        testFile.text = "test1\n"

        def configFile = new File(tempDir, "application.yml")
        configFile.text = "key: val\nkey2: val2\nkey3: val3\n"

        git.add().addFilepattern(".").call()
        git.commit()
                .setMessage("add mixed files")
                .setAuthor(alice)
                .setCommitter(alice)
                .call()

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 1L
        aliceStats.linesAdded() == 6L
        aliceStats.filesChanged() == 3L
        aliceStats.newFiles() == 3L
        aliceStats.productionLinesAdded() == 2L
        aliceStats.testLinesAdded() == 1L
        aliceStats.configLinesAdded() == 3L
    }

    def "should count files changed and new files across commits"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")

        createFileAndCommit("file1.txt", "line1\n", "add file1", alice)
        createFileAndCommit("file2.txt", "line1\n", "add file2", alice)
        createFileAndCommit("file1.txt", "line1\nline2\n", "modify file1", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.commits() == 3L
        aliceStats.filesChanged() == 3L
        aliceStats.newFiles() == 2L
    }

    def "should classify docker-compose files as config"() {
        given:
        def alice = new PersonIdent("Alice", "alice@example.com")
        createFileAndCommit("docker-compose.yml", "version: '3'\n", "add compose", alice)
        createFileAndCommit("docker-compose-dev.yml", "version: '3'\n", "add compose dev", alice)

        when:
        def calculator = new JGitDiffStatsCalculator(git.getRepository())
        def stats = calculator.calculate(LocalDate.of(2020, 1, 1), null, Set.of())

        then:
        def aliceStats = stats.get("Alice")
        aliceStats.configLinesAdded() == 2L
        aliceStats.productionLinesAdded() == 0L
    }

    private void createFileAndCommit(String filename, String content, String message, PersonIdent author) {
        def file = new File(tempDir, filename)
        Files.createDirectories(file.getParentFile().toPath())
        file.text = content
        git.add().addFilepattern(filename).call()
        git.commit()
                .setMessage(message)
                .setAuthor(author)
                .setCommitter(author)
                .call()
    }

    private void deleteFileAndCommit(String filename, String message, PersonIdent author) {
        git.rm().addFilepattern(filename).call()
        git.commit()
                .setMessage(message)
                .setAuthor(author)
                .setCommitter(author)
                .call()
    }
}
