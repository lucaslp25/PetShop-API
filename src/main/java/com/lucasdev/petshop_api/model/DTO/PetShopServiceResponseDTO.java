package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.PetShopService;
import com.lucasdev.petshop_api.model.enums.ServiceType;


import java.math.BigDecimal;

public record PetShopServiceResponseDTO(Long id, String name, ServiceType serviceType, BigDecimal price) {

    public PetShopServiceResponseDTO(PetShopService entity){
        this(entity.getId(), entity.getName(), entity.getServiceType(), entity.getPrice());
    }
}
