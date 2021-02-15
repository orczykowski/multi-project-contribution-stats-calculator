package pl.boringstuff.domain.raport.writer;

import static java.util.function.Function.identity;
import org.springframework.stereotype.Component;
import pl.boringstuff.domain.project.ReportFormat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
