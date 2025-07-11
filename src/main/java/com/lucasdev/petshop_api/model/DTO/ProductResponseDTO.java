package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Product;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id, String name, String description, BigDecimal price, Integer stock) {

    public ProductResponseDTO(Product entity){
        this(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(), entity.getStock());
    }
}