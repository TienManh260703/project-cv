package com.example.exception;

import com.example.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException   {

    @ExceptionHandler(value = DataNoFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataNotFoundException (DataNoFoundException exception){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getMessage());
        apiResponse.setMessage("Data not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
