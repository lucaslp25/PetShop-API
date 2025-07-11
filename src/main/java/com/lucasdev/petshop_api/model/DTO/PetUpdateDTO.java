
package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.model.enums.PetType;
import jakarta.validation.constraints.Size;

public record PetUpdateDTO(
        @Size(min = 1, max = 30)
        String name)
{
    public PetUpdateDTO(Pet entity){
        this(entity.getName());
    }
}
