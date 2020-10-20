package pl.boringstuff.calculator.stats;

import java.math.BigDecimal;
import java.util.Objects;

public record ProjectDistributionStats(
        BigDecimal codeLinesParticipation,
        BigDecimal commitsParticipation,
        BigDecimal filesParticipation) {

  public ProjectDistributionStats(final BigDecimal codeLinesParticipation, final BigDecimal commitsParticipation, final BigDecimal filesParticipation) {
    this.codeLinesParticipation = normalize(codeLinesParticipation);
    this.commitsParticipation = normalize(commitsParticipation);
    this.filesParticipation = normalize(filesParticipation);
  }

  public static ProjectDistributionStats empty() {
    return new ProjectDistributionStats(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
  }

  private BigDecimal normalize(final BigDecimal maybeValue) {
    final var value = Objects.requireNonNullElse(maybeValue, BigDecimal.ZERO);
    if (value.signum() == -1) {
      return BigDecimal.ZERO;
    }
    return value;
  }
}
