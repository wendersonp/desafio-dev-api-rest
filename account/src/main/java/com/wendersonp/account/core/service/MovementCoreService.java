package com.wendersonp.account.core.service;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.MovementRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driving.MovementCoreDrivingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementCoreService implements MovementCoreDrivingPort {

    private final MovementRepositoryDrivenPort movementRepositoryDrivenPort;

    @Override
    public MovementModel registerMovement(AccountModel account, BigDecimal amount, BigDecimal balanceAfter, String description, MovementType type) {
        log.info("Registering movement for account {}", account.getIdentifier());
        MovementModel movementModel = MovementModel.builder()
                .account(account)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .createdAt(LocalDateTime.now())
                .description(description)
                .type(type)
                .build();

        return movementRepositoryDrivenPort.persist(movementModel);
    }

    @Override
    public BigDecimal getTotalWithdrawWithinADate(LocalDate date, UUID accountIdentifier) {
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59, 59);
        return movementRepositoryDrivenPort.getTotalMovementWithinAPeriod(startTime, endTime, accountIdentifier, MovementType.WITHDRAW);
    }
}
