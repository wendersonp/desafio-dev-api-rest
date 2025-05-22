package com.wendersonp.account.application.dto;

import com.wendersonp.account.core.model.enumeration.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record MovementRequestDTO(
        @NotNull
        UUID accountIdentifier,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        MovementType type,

        String description) {
}
