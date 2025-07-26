package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.PetShopService;

public record CreatePetShopServiceDTO(Long serviceId) {

    public CreatePetShopServiceDTO(PetShopService entity) {
        this(entity.getId());
    }

}
