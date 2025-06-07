package ru.yandex.practicum.iteractionapi.circuitBreaker;

public class WarehouseServerUnavailable extends RuntimeException {
    public WarehouseServerUnavailable(String message) {
        super(message);
    }
}
