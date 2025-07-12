package com.lucasdev.petshop_api.services.payment;

import com.lucasdev.petshop_api.model.entities.Payment;

public interface PaymentGateway {

    PaymentResultDTO paymentProcess(Payment payment);
}