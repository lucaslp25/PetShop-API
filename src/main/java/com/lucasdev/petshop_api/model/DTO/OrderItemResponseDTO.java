package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.OrderItem;
import com.lucasdev.petshop_api.services.payment.PaymentResultDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public record OrderItemResponseDTO(

        Long id,
        Integer quantity,
        ProductSummaryDTO product,
        BigDecimal subTotal)
{

    public OrderItemResponseDTO(OrderItem entity){
        this(
                entity.getId(),
                entity.getQuantity(),
                new ProductSummaryDTO(entity.getProduct()),
                entity.getUnitPrice().multiply(new BigDecimal(entity.getQuantity())));
    }

}