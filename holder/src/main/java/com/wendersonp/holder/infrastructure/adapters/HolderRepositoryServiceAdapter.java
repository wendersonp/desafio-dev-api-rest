package com.wendersonp.holder.infrastructure.adapters;

import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.ports.driven.HolderRepositoryDrivenPort;
import com.wendersonp.holder.infrastructure.persistence.entity.HolderEntity;
import com.wendersonp.holder.infrastructure.persistence.repository.HolderEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolderRepositoryServiceAdapter implements HolderRepositoryDrivenPort {

    private final HolderEntityRepository holderEntityRepository;

    @Override
    public HolderModel persist(HolderModel holderModel) {
        log.info("Persisting holder {}", holderModel.getName());

        HolderEntity entity = new HolderEntity(holderModel);
        entity = holderEntityRepository.save(entity);

        holderModel.setIdentifier(entity.getIdentifier());
        return holderModel;
    }

    @Override
    public Optional<HolderModel> findByDocumentHash(byte[] documentHash) {
        return holderEntityRepository.findByDocumentHash(documentHash).map(HolderEntity::toModel);
    }
}
