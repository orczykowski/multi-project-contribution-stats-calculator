package pl.boringstuff.infrastructure.executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("executor")
record ExecutorConfiguration(int numberOfThreads, int queueSize) {

}
