package com.example.exception;

import com.example.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Object>> handleSecurityException(Exception exception) {
        ApiResponse<Object> apiResponse= new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getMessage());
        apiResponse.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(value = DataNoFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataNotFoundException(DataNoFoundException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getMessage());
        apiResponse.setMessage("Data not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationError(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getBody().getDetail());
        List<String> error = fieldErrors.stream().map(
                f -> f.getDefaultMessage()).toList();
        apiResponse.setMessage(error.size() > 1 ? error : error.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<ApiResponse<Object>> handleNumberFormatException (NumberFormatException exception){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getMessage());
        apiResponse.setMessage("Cannot format number");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
