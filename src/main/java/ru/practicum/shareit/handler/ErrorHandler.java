package ru.practicum.shareit.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrResponse handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ErrResponse("404 Not Found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResponse handleValidationException(final ValidateException e) {
        log.info("Validation: {}", e.getMessage());
        return new ErrResponse("Bad request ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResponse handleValidationException(final ConstraintViolationException e) {
        log.info("Validation exception: {}", e.getMessage());
        return new ErrResponse("Bad request ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrResponse handleAccessException(final AccessException e) {
        log.info("Access denied: {}", e.getMessage());
        return new ErrResponse("FORBIDDEN ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrResponse handleConflictException(final ConflictException e) {
        log.info("Conflict: {}", e.getMessage());
        return new ErrResponse("Conflict ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrResponse handleException(final Exception e) {
        log.warn("Error", e);
        return new ErrResponse("Error ", e.getMessage());
    }

}
