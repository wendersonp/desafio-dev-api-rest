package com.wendersonp.holder.infrastructure.web.v1.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiError(Integer code, String type, LocalDateTime timestamp, String message, String reason) {
}
