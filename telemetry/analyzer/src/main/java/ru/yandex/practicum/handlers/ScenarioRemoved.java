package ru.yandex.practicum.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemoved implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro hubEvent) {
        ScenarioRemovedEventAvro scenarioRemovedEvent = (ScenarioRemovedEventAvro) hubEvent.getPayload();
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubEvent.getHubId(), scenarioRemovedEvent.getName());

        if (scenarioOpt.isPresent()) {
            Scenario scenario = scenarioOpt.get();
            log.debug("Найден сценарий для удаления: id {}, имя {}", scenario.getId(), scenario.getName());
            conditionRepository.deleteByScenario(scenario);
            actionRepository.deleteByScenario(scenario);
            log.debug("Удаление сценария c id= {}", scenario.getId());
            scenarioRepository.delete(scenario);
            log.info("Сценарий {} успешно удален вместе с условиями и действиями", scenario.getName());
        } else {
            log.info("Сценарий с именем {} не найден для хаба {}", scenarioRemovedEvent.getName(), hubEvent.getHubId());
        }
    }

    @Override
    public String getMessageType() {
        return ScenarioRemovedEventAvro.class.getSimpleName();
    }
}
