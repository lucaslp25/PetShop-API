package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Product;
import org.eclipse.sisu.Nullable;

import java.math.BigDecimal;

public record ProductSummaryDTO(Long id, String name, BigDecimal price,

                                @Nullable
                                String imageUrl) {

    public ProductSummaryDTO(Product entity){
        this(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl());
    }

}