package com.wendersonp.holder.application.service.impl;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;
import com.wendersonp.holder.application.service.HolderApplicationService;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.ports.driving.HolderServiceDrivingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolderApplicationServiceImpl implements HolderApplicationService {

    private final HolderServiceDrivingPort holderService;


    @Override
    public HolderResponseDTO save(HolderRequestDTO holderRequestDTO) {
        HolderModel holderModel = holderService.save(holderRequestDTO.toModel());
        return new HolderResponseDTO(holderModel);
    }

    @Override
    public HolderResponseDTO findByDocument(String documentNumber) {
        HolderModel holderModel = holderService.findByDocument(documentNumber);
        return new HolderResponseDTO(holderModel);
    }
}
