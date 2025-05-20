package com.wendersonp.holder.core.service;

import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.HolderRequestModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;
import com.wendersonp.holder.core.ports.driven.CryptographyServiceDrivenPort;
import com.wendersonp.holder.core.ports.driven.HolderRepositoryDrivenPort;
import com.wendersonp.holder.core.ports.driving.HolderServiceDrivingPort;
import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolderServiceCore implements HolderServiceDrivingPort {

    private final CryptographyServiceDrivenPort cryptographyServiceDrivenPort;

    private final HolderRepositoryDrivenPort holderRepositoryDrivenPort;

    @Override
    public HolderModel save(HolderRequestModel holderRequestModel) {
        byte[] documentHash = cryptographyServiceDrivenPort.hashDocumentNumber(holderRequestModel.getDocumentNumber());

        findByDocumentHash(documentHash).ifPresent(holderModel -> {
            throw new BusinessException(ExceptionMessageEnum.DOCUMENT_NUMBER_ALREADY_EXISTS);
        });

        String maskedDocumentNumber = cryptographyServiceDrivenPort.maskDocumentNumber(holderRequestModel.getDocumentNumber());
        var holderModel = new HolderModel(documentHash, maskedDocumentNumber, holderRequestModel.getName(), StatusEnum.ACTIVE);
        return holderRepositoryDrivenPort.persist(holderModel);
    }

    @Override
    public HolderModel findByDocument(String documentNumber) throws BusinessException {
        byte[] documentHash = cryptographyServiceDrivenPort.hashDocumentNumber(documentNumber);
        return findByDocumentHash(documentHash)
                .orElseThrow(() -> new BusinessException(ExceptionMessageEnum.DOCUMENT_NUMBER_NOT_FOUND));
    }

    private Optional<HolderModel> findByDocumentHash(byte[] documentHash) {
        return holderRepositoryDrivenPort.findByDocumentHash(documentHash);
    }
}
