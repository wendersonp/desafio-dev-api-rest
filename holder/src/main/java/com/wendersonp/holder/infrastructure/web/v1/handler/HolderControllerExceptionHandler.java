package com.wendersonp.holder.infrastructure.web.v1.handler;


import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.infrastructure.exception.AWSException;
import com.wendersonp.holder.infrastructure.exception.InfrastructureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class HolderControllerExceptionHandler {

    private static final String NOT_FOUND = "not_found";


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        if (ex.getCode().contains(NOT_FOUND)) {
            var errorResponse = ApiError.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .type(ex.getCode())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_NOT_FOUND)).body(errorResponse);
        } else {
            var errorResponse = ApiError.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .type(ex.getCode())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_BAD_REQUEST)).body(errorResponse);
        }
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ApiError> handleInfrastructureException(InfrastructureException ex) {
        if (ex.getCode().contains(NOT_FOUND)) {
            var errorResponse = ApiError.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .type(ex.getCode())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_NOT_FOUND)).body(errorResponse);
        } else {
            var errorResponse = ApiError.builder()
                    .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .type(ex.getCode())
                    .message(ex.getMessage())
                    .reason(ex.getCause().getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_SERVICE_UNAVAILABLE)).body(errorResponse);
        }
    }

    @ExceptionHandler(AWSException.class)
    public ResponseEntity<ApiError> handleAWSException(AWSException ex) {
        var errorResponse = ApiError.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .type(ex.getCode())
                .message(ex.getMessage())
                .reason(ex.getCause().getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_SERVICE_UNAVAILABLE)).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var errorResponse = ApiError.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .type(ex.getClass().getName())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpServletResponse.SC_SERVICE_UNAVAILABLE)).body(errorResponse);
    }
}
