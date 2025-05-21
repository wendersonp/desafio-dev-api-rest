package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.AccountPropertiesModel;
import com.wendersonp.account.core.ports.driven.AccountDefaultPropertiesDrivenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@ConfigurationProperties(prefix = "account")
@RequiredArgsConstructor
public class AccountDefaultPropertiesServiceAdapter implements AccountDefaultPropertiesDrivenPort {

    private String defaultBranch;
    private String withdrawDefaultLimit;

    @Override
    public AccountPropertiesModel getDefaultProperties() {
        return AccountPropertiesModel.builder()
                .defaultBranch(defaultBranch)
                .withdrawDefaultLimit(new BigDecimal(withdrawDefaultLimit).setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
