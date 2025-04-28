package ru.yandex.practicum.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.collector.builders.hub.HubEventBuilder;
import ru.yandex.practicum.collector.builders.sensor.SensorEventBuilder;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;

@Slf4j
@GrpcService
public class CollectorController extends CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventBuilder> sensorBuilders;
    private final Map<HubEventProto.PayloadCase, HubEventBuilder> hubBuilders;

    public CollectorController(
            Map<SensorEventProto.PayloadCase, SensorEventBuilder> sensorBuilders,
            Map<HubEventProto.PayloadCase, HubEventBuilder> hubBuilders) {
        this.sensorBuilders = sensorBuilders;
        this.hubBuilders = hubBuilders;
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (sensorBuilders.containsKey(request.getPayloadCase())) {
                log.info("Обработка события датчика: {}", request);
                sensorBuilders.get(request.getPayloadCase()).builder(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.fromThrowable(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (hubBuilders.containsKey(request.getPayloadCase())) {
                log.info("Обработка события хаба: {}", request);
                hubBuilders.get(request.getPayloadCase()).builder(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.fromThrowable(e)
            ));
        }
    }
}
