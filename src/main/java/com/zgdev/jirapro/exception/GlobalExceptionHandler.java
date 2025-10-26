package com.zgdev.jirapro.exception;

import com.zgdev.jirapro.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex){
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(),"Task not found" ,ex.getMessage()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OperationException.class)
    public ResponseEntity<ErrorResponse> handleOperationException(OperationException ex){
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(),"Something Wrong" ,ex.getMessage()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TaskStatusException.class)
    public ResponseEntity<ErrorResponse> handleTaskStatusNotFoundException(TaskStatusException ex){
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(),"Task status not found" ,ex.getMessage()),HttpStatus.NOT_FOUND);
    }
}
