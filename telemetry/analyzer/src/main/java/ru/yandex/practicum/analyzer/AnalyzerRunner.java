package ru.yandex.practicum.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.processors.HubEventProcessor;
import ru.yandex.practicum.analyzer.processors.SnapshotProcessor;

import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;

    public void run(String... args) {
        log.info("Запуск обработчиков событий и снапшотов...");
        try {
            ExecutorService executorservice = getExecutorService();
            log.debug("Пул потоков успешно создан.");

            executorservice.submit(hubEventProcessor);
            log.info("Обработчик событий хаба (HubEventProcessor) запущен");

            executorservice.submit(snapshotProcessor);
            log.info("Обработчик снапшотов (SnapshotProcessor) запущен");

        } catch (Exception e) {
            log.error("Ошибка при запуске обработчиков: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось запустить обработчики", e);
        }
    }

    private ExecutorService getExecutorService() {
        log.debug("Создание пула потоков для обработки событий...");

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1000,
                1000,
                60L,
                TimeUnit.SECONDS,
                queue,
                new ThreadPoolExecutor.AbortPolicy()
        );
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
