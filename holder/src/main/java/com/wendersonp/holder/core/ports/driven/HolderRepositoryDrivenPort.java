package com.wendersonp.holder.core.ports.driven;

import com.wendersonp.holder.core.model.HolderModel;

import java.util.Optional;

public interface HolderRepositoryDrivenPort {

    HolderModel persist(HolderModel holderModel);

    Optional<HolderModel> findByDocumentHash(byte[] documentHash);
}
