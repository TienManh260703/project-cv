package com.example.config;

import com.example.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        apiResponse.setError(authException.getCause().getMessage());
        apiResponse.setMessage("Token invalid ( expired, malformed ... )");
        mapper.writeValue(response.getWriter(),apiResponse);
    }
}
