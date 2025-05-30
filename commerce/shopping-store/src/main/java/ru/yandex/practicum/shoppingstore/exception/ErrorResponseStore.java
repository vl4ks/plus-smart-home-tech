package ru.yandex.practicum.shoppingstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.iteractionapi.error.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ErrorResponseStore {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleCommonException(RuntimeException e) {
        log.error("500 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .userMessage(e.getMessage())
                .message("Internal Server Error")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException e) {
        log.error("404 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND.name())
                .userMessage(e.getMessage())
                .message("Not Found")
                .build();
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(RuntimeException e) {
        log.error("400 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(e.getMessage())
                .message("Bad request")
                .build();
    }
}
