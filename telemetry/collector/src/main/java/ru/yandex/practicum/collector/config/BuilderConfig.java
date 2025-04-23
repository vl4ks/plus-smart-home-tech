package ru.yandex.practicum.collector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.collector.builders.hub.HubEventBuilder;
import ru.yandex.practicum.collector.builders.sensor.SensorEventBuilder;
import ru.yandex.practicum.collector.schemas.hub.HubEventType;
import ru.yandex.practicum.collector.schemas.sensor.SensorEventType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BuilderConfig {
    @Bean
    public Map<SensorEventType, SensorEventBuilder> sensorBuilders(List<SensorEventBuilder> builders) {
        return builders.stream()
                .collect(Collectors.toMap(SensorEventBuilder::getEventType, Function.identity()));
    }

    @Bean
    public Map<HubEventType, HubEventBuilder> hubBuilders(List<HubEventBuilder> builders) {
        return builders.stream()
                .collect(Collectors.toMap(HubEventBuilder::getEventType, Function.identity()));
    }

}
