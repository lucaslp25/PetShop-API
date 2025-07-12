package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDTO(
        Long id,
        BigDecimal total_price,
        LocalDateTime date,
        PaymentResponseDTO payment,
        List<OrderItemResponseDTO> order_items) {

    public OrderResponseDTO(Order entity){
        this(
                entity.getId(),
                entity.getTotalPrice(),
                entity.getOrderDate(),
                new PaymentResponseDTO(entity.getPayment()),
                entity.getItems().stream().map(OrderItemResponseDTO::new).collect(Collectors.toList()));
    }
}
