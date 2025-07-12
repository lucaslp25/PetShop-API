package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Payment;
import com.lucasdev.petshop_api.model.enums.PaymentMode;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponseDTO(

        Long id,
        PaymentMode paymentMode,
        PaymentStatus status,
        BigDecimal amount
) {
    public PaymentResponseDTO(Payment paymentEntity) {
        this(
                paymentEntity.getId(),
                paymentEntity.getPaymentMode(),
                paymentEntity.getStatus(),
                paymentEntity.getAmount()
        );
    }
}