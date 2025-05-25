package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.ports.driven.HolderServiceDrivenPort;
import com.wendersonp.account.infrastructure.clients.HolderFeignClient;
import com.wendersonp.account.infrastructure.clients.dto.HolderResponseDTO;
import com.wendersonp.account.infrastructure.exception.InfrastructureException;
import com.wendersonp.account.util.ExceptionMessageEnum;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolderClientServiceAdapter implements HolderServiceDrivenPort {

    private final HolderFeignClient holderFeignClient;

    @Override
    public Optional<HolderModel> findByDocument(String documentNumber) {
        try {
            return Optional
                    .of(holderFeignClient.findByDocumentNumber(documentNumber))
                    .map(HolderResponseDTO::toModel);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<HolderModel> findByIdentifier(UUID identifier) {
        try {
            return Optional
                    .of(holderFeignClient.findByIdentifier(identifier))
                    .map(HolderResponseDTO::toModel);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public void removeByIdentifier(UUID identifier) {
        try {
            holderFeignClient.removeByIdentifier(identifier);
        } catch (FeignException.NotFound ex) {
            log.error("Holder not found", ex);
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_NOT_FOUND, ex);
        }
    }

    @Override
    public void reactivate(UUID holderId) {
        try {
            holderFeignClient.reactivate(holderId);
        } catch (FeignException.NotFound ex) {
            log.error("Holder not found", ex);
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_NOT_FOUND, ex);
        }
    }
}
