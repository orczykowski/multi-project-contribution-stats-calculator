package io.github.orczykowski.infrastructure.executor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("executor")
record ExecutorConfiguration(int numberOfThreads, int queueSize) {

}
