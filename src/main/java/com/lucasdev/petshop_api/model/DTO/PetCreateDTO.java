package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.model.enums.PetType;

public record PetCreateDTO(String name, PetType type, Long owner_id) {


    public PetCreateDTO(Pet entity){
        this(entity.getName(), entity.getType(), entity.getOwner().getId());
    }
}
