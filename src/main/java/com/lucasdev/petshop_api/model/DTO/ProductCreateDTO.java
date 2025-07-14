package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreateDTO(

        @NotBlank
        @Size(min = 2, max = 50)
        String name,

        @Size(min = 10, max = 120)
        String description,

        @NotNull(message = "The field 'price' cannot be null.")
        @Positive(message = "The price must be positive")
        BigDecimal price,

        Integer stock
) {

    public ProductCreateDTO(Product entity) {
        this(entity.getName(), entity.getDescription(), entity.getPrice(), entity.getStock());
    }
}

