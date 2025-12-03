package com.emicalculator.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class EmiExceptionalHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            errors.put(fieldName, message);
        });
        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_ERROR");
        body.put("details", errors);
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArg(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "INVALID_INPUT");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "INTERNAL_ERROR");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
