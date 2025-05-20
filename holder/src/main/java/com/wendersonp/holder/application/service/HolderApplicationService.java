package com.wendersonp.holder.application.service;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;

public interface HolderApplicationService {
    HolderResponseDTO save(HolderRequestDTO holderRequestDTO);
    HolderResponseDTO findByDocument(String documentNumber);
}
