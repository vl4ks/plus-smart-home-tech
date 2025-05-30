package ru.yandex.practicum.shoppingcart.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.iteractionapi.error.ErrorResponse;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorResponseCart {
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

    @ExceptionHandler({MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(RuntimeException e) {
        log.error("400 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(e.getMessage())
                .message("Bad request")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(NotAuthorizedUserException e) {
        log.error("401 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.name())
                .userMessage(e.getMessage())
                .message("Not Authorized")
                .build();
    }

    @ExceptionHandler({NoProductsInShoppingCartException.class,
            ProductInShoppingCartIsNotInWarehouse.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(RuntimeException e) {
        log.error("404 {}", e.getMessage());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND.name())
                .userMessage(e.getMessage())
                .message("Not Found")
                .build();
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleFeignException(FeignException e) {
        log.error("Ошибка при вызове сервиса: {}", e.getMessage());

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE.name())
                .userMessage(e.getMessage())
                .message("Сервис склада временно недоступен")
                .build();
    }

}
