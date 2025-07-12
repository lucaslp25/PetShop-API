package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.OrderItem;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemDTO(

        @NotNull(message = "The field 'product_id' cannot be empty")
        Long product_id,

        @NotNull(message = "The field 'quantity' cannot be empty")
        Integer quantity)
{
    public CreateOrderItemDTO(OrderItem entity){
        this(entity.getProduct().getId(), entity.getQuantity());
    }

}