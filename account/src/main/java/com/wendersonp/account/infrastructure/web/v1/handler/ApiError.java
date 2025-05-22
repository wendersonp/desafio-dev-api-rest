package com.wendersonp.account.infrastructure.web.v1.handler;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiError(Integer code, String type, LocalDateTime timestamp, String message) {
}
