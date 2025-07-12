package com.lucasdev.petshop_api.services.payment;

import com.lucasdev.petshop_api.model.entities.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FakeTestPayment implements PaymentGateway {

    @Override
    public PaymentResultDTO paymentProcess(Payment payment) {

        System.out.println("--- PAYMENT GATEWAY ---");
        System.out.println("Processing payment of: " + payment.getAmount());

        boolean success = true; //only for tests

        if (!success){
            String fakeTransactionId = "null";
            String message = "Payment Unauthorized!";
        }

        String fakeTransactionId = "transaction " + UUID.randomUUID().toString();
        String message = "Payment processed successfully!";

        return new PaymentResultDTO(success, fakeTransactionId, message);
    }
}