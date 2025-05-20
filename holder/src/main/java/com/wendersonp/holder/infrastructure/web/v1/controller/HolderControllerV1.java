package com.wendersonp.holder.infrastructure.web.v1.controller;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;
import com.wendersonp.holder.application.service.HolderApplicationService;
import com.wendersonp.holder.infrastructure.web.v1.routes.V1Routes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(V1Routes.HOLDER_PATH)
@RequiredArgsConstructor
public class HolderControllerV1 {

    private final HolderApplicationService holderApplicationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public HolderResponseDTO save(@Valid HolderRequestDTO holderRequestDTO) {
        log.info("Saving holder {}", holderRequestDTO.name());
        return holderApplicationService.save(holderRequestDTO);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public HolderResponseDTO findByDocument(@RequestParam String documentNumber) {
        log.info("Finding holder by document");
        return holderApplicationService.findByDocument(documentNumber);
    }

}
