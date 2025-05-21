package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.AccountRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driven.MovementRepositoryDrivenPort;
import com.wendersonp.account.infrastructure.persistence.entity.AccountEntity;
import com.wendersonp.account.infrastructure.persistence.entity.MovementEntity;
import com.wendersonp.account.infrastructure.persistence.repository.MovementEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementRepositoryServiceAdapter implements MovementRepositoryDrivenPort {

    private final MovementEntityRepository movementRepository;

    @Override
    public MovementModel persist(MovementModel movementModel) {
        return movementRepository.save(new MovementEntity(movementModel)).toModel();
    }

    @Override
    public BigDecimal getTotalMovementWithinAPeriod(LocalDateTime beginningDate, LocalDateTime endingDate, UUID accountIdentifier, MovementType type) {
        return movementRepository.totalMovementBetweenTwoDates(beginningDate, endingDate, accountIdentifier, type.toString());
    }
}
