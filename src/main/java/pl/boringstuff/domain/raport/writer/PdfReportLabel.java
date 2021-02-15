package pl.boringstuff.domain.raport.writer;

import java.util.List;

enum PdfReportLabel {
  GENERAL_INFORMATION("General Information",
          "General information on projects for which statistics are calculated and the period for which the work was taken into stats",
          List.of("#", "PROJECT NAME", "REPOSITORY URL", "EXCLUDED PATHS")),
  TOTAL_CONTRIBUTION("Total contribution",
          "Aggregate statistics for all users by project for given period",
          List.of("#", "PROJECT NAME", "LINES OF CODE", "COMMITS", "FILES")),
  USER_CONTRIBUTION("User contribution",
          "Detailed statistics per user for all projects for given period",
          List.of("#", "USER NAME", "LINES OF CODE", "COMMITS", "FILES")),
  PROJECT_CONTRIBUTION("Project contribution",
          "Detailed statistics on contributions to projects by user",
          List.of("#", "USER NAME", "LINES OF CODE", "COMMITS", "FILES", "% of lines", "% of commits", "% of files"));
  private final String name;
  private final String description;
  private final List tableHeaders;

  PdfReportLabel(final String name, final String description, final List tableHeaders) {
    this.name = name;
    this.description = description;
    this.tableHeaders = tableHeaders;
  }

  String getName() {
    return name;
  }

  String getDescription() {
    return description;
  }

  List<String> getTableHeaders() {
    return tableHeaders;
  }
}
