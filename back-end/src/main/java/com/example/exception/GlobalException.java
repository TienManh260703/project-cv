package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException   {

    @ExceptionHandler(value = DataNoFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException (DataNoFoundException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
