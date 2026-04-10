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

## Tech Stack
- Java 25 (source compatibility), Spring Boot 3.5.6, non-web application (spring.main.web-application-type: NONE)
- JGit for repository cloning and diff analysis
- Groovy + Spock 2.3 for tests (files in src/test/groovy/, *Spec.groovy naming)
- FreeMarker (HTML reports), iText (PDF), Apache Commons CSV (CSV)

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

## Support

If you like this project, I would really appreciate your support.

Maintaining open source takes time, and your support helps keep this project alive and improving.

<a href="https://buymeacoffee.com/tasior" target="_blank">
  <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="217" height="60">
</a>

---
