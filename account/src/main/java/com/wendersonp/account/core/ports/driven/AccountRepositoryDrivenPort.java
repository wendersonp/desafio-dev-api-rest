package com.wendersonp.account.core.ports.driven;

import com.wendersonp.account.core.model.AccountModel;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryDrivenPort {

    AccountModel persist(AccountModel accountModel);

    Boolean exists(UUID holderId);

    Optional<AccountModel> findByIdentifier(UUID identifier);
}
