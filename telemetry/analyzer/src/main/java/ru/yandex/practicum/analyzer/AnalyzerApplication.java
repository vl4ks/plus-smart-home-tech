package ru.yandex.practicum.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalyzerApplication {

    @Value("${threadPool.arrayBlockingQueue.capacity}")
    private final int arrayBlockingQueueCapacity = 2;
    @Value("${threadPool.corePoolSize}")
    private final int threadPoolCorePoolSize = 2;
    @Value("${threadPool.maximumPoolSize}")
    private final int threadPoolMaximumPoolSize = 2;
    @Value("${threadPool.keepAliveTime}")
    private final long threadPoolKeepAliveTime = 60L;

    public static void main(String[] args) {
        SpringApplication.run(AnalyzerApplication.class, args);
    }
}