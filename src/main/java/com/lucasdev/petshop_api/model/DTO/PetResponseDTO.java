package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.model.enums.PetType;

public record PetResponseDTO(Long id, String name, PetType type, CustomerResponseDTO owner) {


    public PetResponseDTO(Pet entity){
        this(entity.getId(), entity.getName(), entity.getType(),
                new CustomerResponseDTO(entity.getOwner())); // <-- better practice than @JsonIgnore for broken the infinity cycle.
    }
}
