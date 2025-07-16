package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.PetShopOrderService;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record PetShopOrderServiceResponseDTO(

        Long id,
        Instant date,
        BigDecimal subTotal,
        @Column(nullable = true)
        List<PetShopServiceResponseDTO> services

) {

    public PetShopOrderServiceResponseDTO(PetShopOrderService entity){
        this(

                entity.getId(),
                entity.getDate(),
                entity.getTotalPrice(),
                entity.getServices().stream().
                        map(PetShopServiceResponseDTO::new)
                        .collect(Collectors.toList()));
    }
}