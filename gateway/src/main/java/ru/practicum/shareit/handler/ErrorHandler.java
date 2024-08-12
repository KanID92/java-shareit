package ru.practicum.shareit.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResponse handleValidationException(final ConstraintViolationException e) {
        log.info("Validation exception: {}", e.getMessage());
        return new ErrResponse("Bad request ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrResponse handleException(final Exception e) {
        log.warn("Error", e);
        return new ErrResponse("Error ", e.getMessage());
    }

}
