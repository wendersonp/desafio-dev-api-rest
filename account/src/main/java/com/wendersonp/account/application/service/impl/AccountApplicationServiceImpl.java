package com.wendersonp.account.application.service.impl;

import com.wendersonp.account.application.dto.AccountResponseDTO;
import com.wendersonp.account.application.dto.BalanceResponseDTO;
import com.wendersonp.account.application.dto.MovementRequestDTO;
import com.wendersonp.account.application.service.AccountApplicationService;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import com.wendersonp.account.core.ports.driving.AccountCoreDrivingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountApplicationServiceImpl implements AccountApplicationService {

    private final AccountCoreDrivingPort accountCoreService;

    @Override
    public AccountResponseDTO createAccount(String documentNumber) {
        return new AccountResponseDTO(accountCoreService.createAccount(documentNumber));
    }

    @Override
    public AccountResponseDTO findByIdentifier(UUID identifier) {
        return new AccountResponseDTO(accountCoreService.findByIdentifier(identifier));
    }

    @Override
    public BalanceResponseDTO makeMovementOnBalance(MovementRequestDTO movementRequestDTO) {
        var balanceModel = accountCoreService.makeMovementOnBalance(
                movementRequestDTO.accountIdentifier(),
                movementRequestDTO.amount(),
                movementRequestDTO.type(),
                movementRequestDTO.description()
        );
        return new BalanceResponseDTO(balanceModel);
    }

    @Override
    public AccountResponseDTO blockAccount(UUID identifier) {
        return new AccountResponseDTO(accountCoreService.setBlockStatus(identifier, BlockStatus.BLOCKED));
    }

    @Override
    public AccountResponseDTO unblockAccount(UUID identifier) {
        return new AccountResponseDTO(accountCoreService.setBlockStatus(identifier, BlockStatus.UNBLOCKED));
    }
}
