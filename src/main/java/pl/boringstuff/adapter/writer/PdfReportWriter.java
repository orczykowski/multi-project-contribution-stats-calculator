package pl.boringstuff.adapter.writer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.CalculationSpecificationSupplier;
import pl.boringstuff.core.project.ReportFormat;
import pl.boringstuff.core.raport.ContributionReport;
import pl.boringstuff.core.raport.ReportWriter;
import pl.boringstuff.core.stats.model.ContributionStats;
import pl.boringstuff.core.stats.model.UserContributionStats;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Font.FontFamily.HELVETICA;
import static com.itextpdf.text.Font.ITALIC;
import static pl.boringstuff.adapter.writer.PdfReportLabel.*;
import static pl.boringstuff.core.project.ReportFormat.PDF;

@Component
class PdfReportWriter implements ReportWriter {

    private static final String REPORT_TITLE = "Multi project contribution stats report";

    private final CalculationSpecificationSupplier specificationSupplier;

    PdfReportWriter(final CalculationSpecificationSupplier specificationSupplier) {
        this.specificationSupplier = specificationSupplier;
  }

  @Override
  public void write(final ContributionReport report) {
    final var document = new Document(PageSize.A4, 30, 30, 40, 35);
    try {
      openDocument(report, document);
      document.add(createReportTitle());
      document.add(createGeneralInformationSection(report));
        document.add(totalContribution(report));
        document.add(userContribution(report));
        document.add(createProjectContribution(report));
    } catch (FileNotFoundException | DocumentException ex) {
        throw new ReportException(ex.getMessage(), PDF);
    } finally {
        document.close();
    }
  }

    @Override
    public ReportFormat format() {
        return PDF;
    }

    private void openDocument(final ContributionReport report, final Document document)
            throws FileNotFoundException, DocumentException {
        final FileOutputStream outputStream = new FileOutputStream(resultDirectoryPath(report));
        PdfWriter.getInstance(document, outputStream);
        document.open();
    }

    private Paragraph createReportTitle() {
        final var paragraph = emptyLines(1);
        final var title = new Paragraph(REPORT_TITLE, new Font(HELVETICA, 22, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(emptyLines(4));
        paragraph.add(title);
        return paragraph;
    }

    private Element userContribution(final ContributionReport report) {
        final List<UserContributionStats> stats = report.totalUserContributionStats();
        var values = Stream.of();
        for (int i = 0; i < stats.size(); i++) {
            values = Stream.concat(values, countUserStatsAsStreamValues(i, stats.get(i)));
        }
    return createSingleStatsPart(USER_CONTRIBUTION, values.map(Object::toString));
  }

  private Element totalContribution(final ContributionReport report) {
      final var projectStats = report.projectContributionStats();
      var streamsValues = new ArrayList<Stream<Object>>();
      for (int i = 0; i < projectStats.size(); i++) {
          final var stat = projectStats.get(i);
          streamsValues.add(Stream.of(i + 1, stat.project().getName()));
          streamsValues.add(contributionStatsAsStreamValues(stat.total()));
      }

      streamsValues.add(Stream.concat(Stream.of(projectStats.size()),
              contributionStatsAsStreamValues(report.totalContribution())));

      final var values = streamsValues.stream()
              .reduce(Stream::concat)
              .orElseGet(Stream::empty)
              .map(Object::toString);

      return createSingleStatsPart(TOTAL_CONTRIBUTION, values);
  }

  private Element createGeneralInformationSection(final ContributionReport report) {
      final var generalInformationParagraph = reportPartHeader(GENERAL_INFORMATION);
      generalInformationParagraph.add(datesInfoParagraph(report));
      generalInformationParagraph.add(emptyLines(1));

      final var table = createTable(GENERAL_INFORMATION.getTableHeaders(),
              projectDefinitionsAsValueList(report));
      generalInformationParagraph.add(table);
      return generalInformationParagraph;
  }

  private Element createProjectContribution(final ContributionReport report) {
      final var projectContributionParagraph = reportPartHeader(PROJECT_CONTRIBUTION);

      report.projectContributionStats()
              .stream()
              .map(it -> singleProjectContributionParagraph(it.project().getName(), it.userStats()))
              .forEach(projectContributionParagraph::add);

      return projectContributionParagraph;
  }

    private Element singleProjectContributionParagraph(final String name,
                                                       final List<UserContributionStats> userStats) {
        final String replace = name.toUpperCase();
        final var paragraph = new Paragraph(replace, new Font(HELVETICA, 14, BOLD));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(emptyLines(1));
        var values = Stream.of();
        for (int i = 0; i < userStats.size(); i++) {
            values = Stream.concat(values, userProjectDetailsStats(i, userStats.get(i)));
        }
        paragraph.add(
                createTable(PROJECT_CONTRIBUTION.getTableHeaders(), values.map(Object::toString)));
        return paragraph;
    }

    private Paragraph reportPartHeader(final PdfReportLabel label) {
        final var reportPartHeaderParagraph = new Paragraph(label.getName(),
                new Font(HELVETICA, 16, Font.BOLD));
        reportPartHeaderParagraph.setAlignment(Element.ALIGN_LEFT);

        final var descriptionParagraph = new Paragraph(label.getDescription(),
                new Font(HELVETICA, 10, ITALIC));
        descriptionParagraph.setAlignment(Element.ALIGN_LEFT);
        reportPartHeaderParagraph.add(descriptionParagraph);

        reportPartHeaderParagraph.add(emptyLines(1));

        return reportPartHeaderParagraph;
    }

    private Stream<Object> countUserStatsAsStreamValues(final int idx,
                                                        final UserContributionStats userStat) {
        return Stream.concat(Stream.of(idx + 1, userStat.user().name()),
                contributionStatsAsStreamValues(userStat.counts()));
    }

    private Stream<Object> userProjectDetailsStats(final int idx,
                                                   final UserContributionStats userStat) {
        final var counts = userStat.counts();
        final var distribution = userStat.distribution();
        return Stream.of(idx + 1, userStat.user().name(),
                counts.lineOfCode(), counts.commits(), counts.files(),
                distribution.codeLinesParticipation(), distribution.commitsParticipation(),
                distribution.filesParticipation());
    }

    private Stream<Object> contributionStatsAsStreamValues(final ContributionStats stats) {
        return Stream.of(stats.lineOfCode(), stats.commits(), stats.files());
    }

    private Stream<String> projectDefinitionsAsValueList(final ContributionReport report) {
        final var projectStats = report.projectContributionStats();
        var values = Stream.of();
        for (int i = 0; i < projectStats.size(); i++) {
            var project = projectStats.get(i).project();
            values = Stream.concat(values, Stream.of(i + 1, project.getName(), project.getRepositoryUrl(),
                    project.getJoinedExcludedPaths(",")));
        }
    return values.map(Object::toString);
  }

  private Element createSingleStatsPart(final PdfReportLabel label, final Stream<String> values) {
    final var paragraph = reportPartHeader(label);
    final var table = createTable(label.getTableHeaders(), values);
    paragraph.add(table);
    return paragraph;
  }

  private Element datesInfoParagraph(final ContributionReport report) {
      final var datesInfo = new Paragraph();
      datesInfo.add(dateInfoParagraph("calculation date: ", report.calculationDate().toString()));
      datesInfo.add(
              dateInfoParagraph("start of the calculation period: ", report.dateFrom().toString()));
      return datesInfo;
  }

  private Paragraph dateInfoParagraph(final String label, final String formattedDate) {
    var paragraph = new Paragraph();
    paragraph.add(new Phrase(label, new Font(HELVETICA, 11, ITALIC)));
    paragraph.add(new Phrase(formattedDate, new Font(HELVETICA, 12, BOLD)));
    return paragraph;
  }

  private Paragraph emptyLines(int number) {
    final var paragraph = new Paragraph(" ");
    for (int i = 1; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
    return paragraph;
  }

  private Paragraph createTable(final List<String> headerLabels, final Stream<String> values) {
      final var paragraph = new Paragraph();
      final var table = new PdfPTable(headerLabels.size());
      table.setWidthPercentage(100);
      final var headerCells = headerLabels.stream().sequential().map(this::asHeaderCell);
      final var valuesCells = values.map(value -> createCell(value, Font.NORMAL));

      Stream.of(headerCells, valuesCells)
              .flatMap(it -> it)
              .forEach(table::addCell);

      paragraph.add(table);
      paragraph.add(emptyLines(2));
      return paragraph;
  }

  private PdfPCell asHeaderCell(final String header) {
    final var pdfCell = createCell(header, BOLD);
    pdfCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
    return pdfCell;
  }

  private PdfPCell createCell(final String value, final int fontWeight) {
    final var phrase = new Phrase(value, new Font(HELVETICA, 12, fontWeight));
    final var pdfPCell = new PdfPCell(phrase);
    pdfPCell.setMinimumHeight(25);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    return pdfPCell;
  }

  private String resultDirectoryPath(final ContributionReport report) {
    return specificationSupplier.supply().reportPath(report.calculationDate()).toString();
  }
}
