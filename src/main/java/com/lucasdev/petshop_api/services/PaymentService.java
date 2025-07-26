package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.model.entities.Payment;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.PaymentRepository;
import com.lucasdev.petshop_api.services.payment.MercadoPagoPaymentGateway;
import com.lucasdev.petshop_api.services.payment.PaymentGateway;
import com.lucasdev.petshop_api.services.payment.PreferenceCreationResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final MercadoPagoPaymentGateway mercadoPagoPaymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway, MercadoPagoPaymentGateway mercadoPagoPaymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.mercadoPagoPaymentGateway = mercadoPagoPaymentGateway;
    }

    @Transactional
    public PreferenceCreationResultDTO initPayment(Payment ref) {

        return mercadoPagoPaymentGateway.paymentProcess(ref);
    }

}
