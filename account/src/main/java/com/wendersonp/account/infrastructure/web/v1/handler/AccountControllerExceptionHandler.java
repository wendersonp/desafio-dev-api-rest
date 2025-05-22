package com.wendersonp.account.infrastructure.web.v1.handler;

import com.wendersonp.account.core.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AccountControllerExceptionHandler {

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
}
