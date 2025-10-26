package com.zgdev.jirapro.exception;

import com.zgdev.jirapro.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        LOG.warn("Task not found: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Task not found", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<ErrorResponse> handleOperationException(OperationException ex) {
        LOG.error("Operation failed: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Operation failed", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TaskStatusException.class)
    public ResponseEntity<ErrorResponse> handleTaskStatusNotFoundException(TaskStatusException ex) {
        LOG.warn("Task status not found: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Task status not found", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        LOG.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        LOG.error("Unhandled exception occurred", ex);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", "An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
