package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.fixture.AccountFixture;
import com.wendersonp.account.core.fixture.AccountPropertiesFixture;
import com.wendersonp.account.core.fixture.BalanceFixture;
import com.wendersonp.account.core.fixture.HolderFixture;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.AccountPropertiesModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.model.enumeration.StatusEnum;
import com.wendersonp.account.core.ports.driven.AccountDefaultPropertiesDrivenPort;
import com.wendersonp.account.core.ports.driven.AccountRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driven.HolderServiceDrivenPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCoreServiceTest {

    @Mock
    private HolderServiceDrivenPort holderService;

    @Mock
    private BalanceCoreService balanceCoreService;

    @Mock
    private AccountRepositoryDrivenPort accountRepository;

    @Mock
    private AccountDefaultPropertiesDrivenPort accountDefaultPropertiesDrivenPort;

    @InjectMocks
    private AccountCoreService accountCoreService;

    private static String documentNumber;

    private static HolderModel holderModel;

    private static AccountModel accountModel;

    private static AccountPropertiesModel accountPropertiesModel;

    private static BalanceModel balanceModel;

    @BeforeEach
    void setUp() {
        documentNumber = "12345678944";
        holderModel = HolderFixture.getHolderModel();
        accountModel = AccountFixture.getAccountModel();
        accountPropertiesModel = AccountPropertiesFixture.getAccountProperties();
        balanceModel = BalanceFixture.getBalanceModel();
        accountModel.setHolderId(holderModel.getIdentifier());
        accountModel.setBalance(() -> balanceModel);
    }


    @Test
    void mustNotCreateAccountWithHolderNotFound() {
        when(holderService.findByDocument(documentNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.createAccount(documentNumber));
        assertEquals(ExceptionMessageEnum.HOLDER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotCreateAccountWithHolderNotActive() {
        holderModel.setStatus(StatusEnum.INACTIVE);
        when(holderService.findByDocument(documentNumber)).thenReturn(Optional.of(holderModel));

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.createAccount(documentNumber));
        assertEquals(ExceptionMessageEnum.HOLDER_INACTIVE.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotCreateAccountWhenItAlreadyExists() {
        accountModel.setHolderId(holderModel.getIdentifier());

        when(holderService.findByDocument(documentNumber)).thenReturn(Optional.of(holderModel));
        when(accountRepository.exists(holderModel.getIdentifier())).thenReturn(true);

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.createAccount(documentNumber));
        assertEquals(ExceptionMessageEnum.ACCOUNT_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    void mustCreateAccountSuccessfully() {
        when(holderService.findByDocument(documentNumber)).thenReturn(Optional.of(holderModel));
        when(accountRepository.exists(holderModel.getIdentifier())).thenReturn(false);
        when(accountDefaultPropertiesDrivenPort.getDefaultProperties()).thenReturn(accountPropertiesModel);
        when(balanceCoreService.createBalance()).thenReturn(balanceModel);
        when(accountRepository.persist(any(AccountModel.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        var accountCreated = accountCoreService.createAccount(documentNumber);

        assertEquals(holderModel.getIdentifier(), accountCreated.getHolderId());
        assertNotNull(accountCreated.getAccountNumber());
        assertEquals(accountPropertiesModel.getDefaultBranch(), accountCreated.getBranch());
        assertEquals(accountPropertiesModel.getWithdrawDefaultLimit(), accountCreated.getWithdrawDailyLimit());
        assertEquals(BlockStatus.UNBLOCKED, accountCreated.getStatus());
        assertEquals(balanceModel, accountCreated.getBalance().get());
    }

    @Test
    void mustNotMakeMovementWithAccountNotFound() {
        var identifier = accountModel.getIdentifier();
        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.makeMovementOnBalance(identifier, BigDecimal.TEN, MovementType.DEPOSIT, "description"));

        assertEquals(ExceptionMessageEnum.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }
    @Test
    void mustNotMakeMovementWithBlockedAccount() {
        var identifier = accountModel.getIdentifier();
        accountModel.setStatus(BlockStatus.BLOCKED);
        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.of(accountModel));

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.makeMovementOnBalance(identifier, BigDecimal.TEN, MovementType.DEPOSIT, "description"));

        assertEquals(ExceptionMessageEnum.BLOCKED_ACCOUNT.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotMakeMovementWithNotFoundHolder() {
        var identifier = accountModel.getIdentifier();
        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.of(accountModel));
        when(holderService.findByIdentifier(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.makeMovementOnBalance(identifier, BigDecimal.TEN, MovementType.DEPOSIT, "description"));

        assertEquals(ExceptionMessageEnum.HOLDER_INACTIVE.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotMakeMovementWithHolderInactive() {
        var identifier = accountModel.getIdentifier();
        holderModel.setStatus(StatusEnum.INACTIVE);

        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.of(accountModel));
        when(holderService.findByIdentifier(any())).thenReturn(Optional.of(holderModel));

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.makeMovementOnBalance(identifier, BigDecimal.TEN, MovementType.DEPOSIT, "description"));

        assertEquals(ExceptionMessageEnum.HOLDER_INACTIVE.getMessage(), exception.getMessage());
    }

    @Test
    void mustMakeMovementSuccessfully() {
        var identifier = accountModel.getIdentifier();

        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.of(accountModel));
        when(holderService.findByIdentifier(any())).thenReturn(Optional.of(holderModel));
        when(balanceCoreService.makeMovementOnBalance(any(), any(), any(), any(), any())).thenReturn(accountModel.getBalance().get());

        var balanceResult = accountCoreService.makeMovementOnBalance(identifier, BigDecimal.TEN, MovementType.DEPOSIT, "description");

        assertNotNull(balanceResult);
        assertEquals(balanceModel, balanceResult);
    }

    @Test
    void mustNotSetBlockStatusWithAccountNotFound() {
        var identifier = accountModel.getIdentifier();
        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> accountCoreService.setBlockStatus(identifier, BlockStatus.BLOCKED));

        assertEquals(ExceptionMessageEnum.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void mustSetBlockStatusSuccessfully() {
        var identifier = accountModel.getIdentifier();
        when(accountRepository.findByIdentifier(any())).thenReturn(Optional.of(accountModel));
        when(accountRepository.persist(any(AccountModel.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        var accountBlocked = accountCoreService.setBlockStatus(identifier, BlockStatus.BLOCKED);
        assertEquals(BlockStatus.BLOCKED, accountBlocked.getStatus());
    }
}