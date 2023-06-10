package com.checkme.CheckMe.exception;

import com.checkme.CheckMe.email.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandle {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSenderService.class);

    // Handle validation exception
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LOGGER.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // Handle SQL exception
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public Map<String, String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        LOGGER.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());
        return errors;
    }

    // Handle Bad request exception
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handleBadRequestException(BadRequestException ex) {
        LOGGER.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    // Handle Resource not found exception
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        LOGGER.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    // Handle Http Message Not Readable Exception
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.info(ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Required request body is missing");
        return errors;
    }
}
