package ru.yandex.practicum.analyzer.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.repository.ActionRepository;
import ru.yandex.practicum.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAdded implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro hubEvent) {
        ScenarioAddedEventAvro scenarioAddedEvent = (ScenarioAddedEventAvro) hubEvent.getPayload();
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubEvent.getHubId(),
                scenarioAddedEvent.getName()).orElseGet(() -> {
            log.debug("Сценарий не найден, создание нового");
            return scenarioRepository.save(buildToScenario(hubEvent));
        });

        if (checkSensorsInScenarioConditions(scenarioAddedEvent, hubEvent.getHubId())) {
            Set<Condition> conditions = buildToCondition(scenarioAddedEvent, scenario);
            conditionRepository.saveAll(conditions);
            log.info("Сохранено {} условий для сценария", conditions.size());
        }
        if (checkSensorsInScenarioActions(scenarioAddedEvent, hubEvent.getHubId())) {
            Set<Action> actions = buildToAction(scenarioAddedEvent, scenario);
            actionRepository.saveAll(actions);
            log.info("Сохранено {} действий для сценария", actions.size());
        }
        log.info("Сценарий успешно обработан");
    }

    @Override
    public String getMessageType() {
        return ScenarioAddedEventAvro.class.getSimpleName();
    }

    private Scenario buildToScenario(HubEventAvro hubEvent) {
        ScenarioAddedEventAvro scenarioAddedEvent = (ScenarioAddedEventAvro) hubEvent.getPayload();
        return Scenario.builder()
                .name(scenarioAddedEvent.getName())
                .hubId(hubEvent.getHubId())
                .build();
    }

    private Set<Condition> buildToCondition(ScenarioAddedEventAvro scenarioAddedEvent, Scenario scenario) {
        return scenarioAddedEvent.getConditions().stream()
                .map(c -> Condition.builder()
                        .sensor(sensorRepository.findById(Long.valueOf(c.getSensorId())).orElseThrow())
                        .scenario(scenario)
                        .type(c.getType())
                        .operation(c.getOperation())
                        .value(setValue(c.getValue()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<Action> buildToAction(ScenarioAddedEventAvro scenarioAddedEvent, Scenario scenario) {
        return scenarioAddedEvent.getActions().stream()
                .map(action -> Action.builder()
                        .sensor(sensorRepository.findById(Long.valueOf(action.getSensorId())).orElseThrow())
                        .scenario(scenario)
                        .type(action.getType())
                        .value(action.getValue())
                        .build())
                .collect(Collectors.toSet());
    }

    private Integer setValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return (Boolean) value ? 1 : 0;
        }
    }

    private Boolean checkSensorsInScenarioConditions(ScenarioAddedEventAvro scenarioAddedEvent, String hubId) {
        return sensorRepository.existsByIdInAndHubId(scenarioAddedEvent.getConditions().stream()
                .map(ScenarioConditionAvro::getSensorId)
                .toList(), hubId);
    }

    private Boolean checkSensorsInScenarioActions(ScenarioAddedEventAvro scenarioAddedEvent, String hubId) {
        return sensorRepository.existsByIdInAndHubId(scenarioAddedEvent.getActions().stream()
                .map(DeviceActionAvro::getSensorId)
                .toList(), hubId);
    }
}
