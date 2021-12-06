package pl.boringstuff.adapter.writer;

import org.springframework.stereotype.Component;
import pl.boringstuff.core.project.ReportFormat;
import pl.boringstuff.core.raport.ReportWriter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
public class ReportWriterProvider {

  private final Map<ReportFormat, ReportWriter> writers;

  ReportWriterProvider(final List<ReportWriter> writers) {
    this.writers = writers.stream()
            .collect(Collectors.toUnmodifiableMap(ReportWriter::format, identity()));
  }

  public ReportWriter provideFor(final ReportFormat format) {
    return writers.get(format);
  }
}
