package com.wendersonp.holder.core.ports.driven;

import com.wendersonp.holder.core.model.HolderModel;

import java.util.Optional;
import java.util.UUID;

public interface HolderRepositoryDrivenPort {

    HolderModel persist(HolderModel holderModel);

    Optional<HolderModel> findByDocumentHash(byte[] documentHash);

    Optional<HolderModel> findByIdentifier(UUID identifier);
}
