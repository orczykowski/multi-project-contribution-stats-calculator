package pl.boringstuff.calculator.stats;

import java.math.BigDecimal;
import java.util.Objects;

public record UserProjectDistributionStats(
        BigDecimal codeLinesParticipation,
        BigDecimal commitsParticipation,
        BigDecimal filesParticipation) {

  public UserProjectDistributionStats(final BigDecimal codeLinesParticipation, final BigDecimal commitsParticipation, final BigDecimal filesParticipation) {
    this.codeLinesParticipation = normalize(codeLinesParticipation);
    this.commitsParticipation = normalize(commitsParticipation);
    this.filesParticipation = normalize(filesParticipation);
  }

  public static UserProjectDistributionStats empty() {
    return new UserProjectDistributionStats(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
  }

  private BigDecimal normalize(final BigDecimal value) {
    return Objects.requireNonNullElse(value, BigDecimal.ZERO).abs();
  }
}
