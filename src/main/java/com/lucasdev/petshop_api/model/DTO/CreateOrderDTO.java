package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.enums.PaymentMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDTO(

        @NotNull(message = "The field 'customer_id' cannot be null.")
        Long customer_id,

        @NotNull(message = "The order need´s a paymentMode")
        PaymentMode payment_mode,

        @NotEmpty(message = "The order_items must have at least one item")
        List<CreateOrderItemDTO> order_items)
{

}

