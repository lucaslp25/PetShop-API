package com.lucasdev.petshop_api.services.payment;

public record PaymentResultDTO(boolean success, String transactionId, String message) {

}
