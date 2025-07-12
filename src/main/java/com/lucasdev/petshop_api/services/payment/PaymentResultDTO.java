package com.lucasdev.petshop_api.services.payment;

import com.lucasdev.petshop_api.model.entities.Payment;

public record PaymentResultDTO(boolean success, String transactionId, String message) {

}
