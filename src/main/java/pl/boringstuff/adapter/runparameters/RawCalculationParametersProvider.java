package pl.boringstuff.adapter.runparameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.boringstuff.core.CalculationSpecificationSupplier;

import java.nio.file.FileSystems;
import java.time.LocalDate;

@Component
class RawCalculationParametersProvider implements CalculationSpecificationSupplier {

    private static final Logger log = LoggerFactory.getLogger(RawCalculationParametersProvider.class);

    private final CalculationSpecification specification;

    RawCalculationParametersProvider(final DefaultCalculationParameters defaultParameters) {
        this.specification = new CalculationSpecification(
                LocalDate.parse(defaultParameters.dateFrom()),
                defaultParameters.resultDir(),
                defaultParameters.repoPath(),
                defaultParameters.reportFormat(),
                defaultParameters.workingDir(),
                defaultParameters.timoutInSeconds());
        log.info("run with configuration {}", this.specification);
    }

    @Override
    public CalculationSpecification supply() {
        return specification;
    }
}
