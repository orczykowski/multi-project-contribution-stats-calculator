# Multi-Project Contribution Stats Calculator

A command-line tool that calculates contribution statistics across multiple Git repositories and generates comprehensive reports. Built with Spring Boot and [JGit](https://www.eclipse.org/jgit/) for native Git analysis — no external Git tools required.

## Features

- **Multi-repository analysis** — analyze contributions across any number of Git repositories in a single run
- **Per-contributor breakdown** — commits, lines added, lines removed, files changed, production vs test code, and new files
- **Project distribution** — percentage of each contributor's share per project
- **Aggregated totals** — combined stats across all projects per contributor
- **Multiple report formats** — HTML (with search), CSV, PDF
- **Configurable date range** — analyze contributions from a specific start date
- **Path exclusions** — exclude directories (e.g., generated code, vendor) per project
- **Parallel execution** — concurrent repository analysis with configurable thread pool

## Requirements

- [Java 21+](https://openjdk.java.net/projects/jdk/21/)
- SSH key or credentials configured for your Git hosting provider
  ([GitHub SSH setup](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account))

## Quick Start

1. Define your repositories in `projects.json` (see [Project Configuration](#project-configuration))
2. Run the calculator:

```bash
./gradlew clean bootRun -Pargs=--run.dateFrom="2024-01-01"
```

3. Find the generated report in the `reports/` directory

## Usage

```bash
./gradlew clean bootRun -Pargs=ARGS
```

Multiple arguments are comma-separated:

```bash
./gradlew clean bootRun -Pargs=--run.dateFrom="2024-01-01",--run.reportFormat=CSV
```

### Parameters

| Parameter | Required | Description | Default |
|---|---|---|---|
| `--run.dateFrom` | No | Start date for analysis (`yyyy-MM-dd`) | `1970-01-01` |
| `--run.resultDir` | No | Output directory for reports | `reports` |
| `--run.repoPath` | No | Path to project definitions JSON file | `projects.json` |
| `--run.reportFormat` | No | Report format: `HTML`, `CSV`, or `PDF` | `HTML` |
| `--run.timoutInSeconds` | No | Timeout per repository analysis (seconds) | `3600` |
| `--run.workingDir` | No | Temporary directory for cloned repositories | `/tmp/multi-project-contributions-stats-calculator-working-dir/` |
| `--run.numberOfThreads` | No | Number of concurrent analysis threads | `10` |
| `--run.queueSize` | No | Thread pool queue size | `40` |

### Examples

```bash
# HTML report (default) with search functionality
./gradlew clean bootRun -Pargs=--run.dateFrom="2024-01-01"

# CSV report
./gradlew clean bootRun -Pargs=--run.dateFrom="2024-01-01",--run.reportFormat=CSV

# PDF report with custom output directory
./gradlew clean bootRun -Pargs=--run.dateFrom="2024-01-01",--run.reportFormat=PDF,--run.resultDir="output"
```

## Project Configuration

Define repositories to analyze in a JSON file (default: `projects.json`):

```json
{
  "projects": [
    {
      "url": "git@github.com:your-org/project-one.git",
      "excludePaths": [
        "docs/",
        "generated/"
      ]
    },
    {
      "url": "git@github.com:your-org/project-two.git",
      "excludePaths": []
    }
  ]
}
```

- `url` — SSH or HTTPS repository URL
- `excludePaths` — list of path prefixes to exclude from analysis

## Report Contents

Each report includes:

| Metric | Description |
|---|---|
| Commits | Number of commits per contributor |
| Lines added | Total lines added |
| Lines removed | Total lines removed |
| Files changed | Number of unique files touched |
| Production lines | Lines added in production code (non-test directories) |
| Test lines | Lines added in test directories (`src/test/`, `test/`) |
| New files | Files created (not modified) |
| Distribution | Percentage share of commits, lines added, lines removed, and files changed per project |

The HTML report includes a search bar for filtering results by contributor name.

## Development

### Build

```bash
./gradlew clean build
```

### Run Tests

```bash
./gradlew clean test
```

### Tech Stack

- Java 21, Spring Boot 3.3
- JGit for repository analysis
- Groovy + Spock for testing
- FreeMarker (HTML), iText (PDF), Apache Commons CSV (CSV)
