package com.openfgademo.api.net.adviser;

import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.openfgademo.api.models.common.AppException;
import com.openfgademo.api.models.common.AppErrorResponse;

@RestControllerAdvice
public class AppExceptionAdvisor {

    @ExceptionHandler(AppException.class)
    @Order(1)
    public ResponseEntity<AppErrorResponse> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(AppErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .build());
    }
}