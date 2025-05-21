package com.wendersonp.account.infrastructure.clients;

import com.wendersonp.account.infrastructure.clients.dto.HolderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "holder-service")
public interface HolderFeignClient {

    @GetMapping(value = "/holder", produces = "application/json")
    HolderResponseDTO findByDocumentNumber(@RequestParam String documentNumber);

    @GetMapping(value = "/holder/{identifier}", produces = "application/json")
    HolderResponseDTO findByIdentifier(@PathVariable UUID identifier);
}
