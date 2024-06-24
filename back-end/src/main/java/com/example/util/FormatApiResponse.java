package com.example.util;

import com.example.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatApiResponse implements ResponseBodyAdvice {
    //    Bất cứ phẩn hổi nào cũng ghi đè : true => chạy xuống beforeBodyWrite
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        Integer status = servletResponse.getStatus();
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        apiResponse.setStatusCode(status);

        if (status >= 400) {
            // case error
            return body;
        } else {
            // case success
            apiResponse.setMessage("CALL API SUCCESS");
            apiResponse.setData(body);
        }
        return apiResponse;
    }
}
