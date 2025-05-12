package ru.yandex.practicum.iteractionapi.error;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorResponse {
    Throwable cause;
    List<StackTraceElement> stackTrace;
    String httpStatus;
    String userMessage;
    String message;
    List<Throwable> suppressed;
    String localizedMessage;
}
