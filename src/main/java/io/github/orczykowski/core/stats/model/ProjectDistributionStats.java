package io.github.orczykowski.core.stats.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ProjectDistributionStats(
        BigDecimal commitsParticipation,
        BigDecimal linesAddedParticipation,
        BigDecimal linesRemovedParticipation) {

    public ProjectDistributionStats(final BigDecimal commitsParticipation,
                                    final BigDecimal linesAddedParticipation,
                                    final BigDecimal linesRemovedParticipation) {
        this.commitsParticipation = normalize(commitsParticipation);
        this.linesAddedParticipation = normalize(linesAddedParticipation);
        this.linesRemovedParticipation = normalize(linesRemovedParticipation);
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
