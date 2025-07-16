package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.model.entities.Order;
import com.lucasdev.petshop_api.model.entities.Payment;
import com.lucasdev.petshop_api.model.entities.Sale;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.PaymentRepository;
import com.lucasdev.petshop_api.services.payment.PaymentGateway;
import com.lucasdev.petshop_api.services.payment.PaymentResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public PaymentResultDTO executePaymentForOrder(Sale sale){

        Payment payment = sale.getPayment(); //get the payment info

        payment.setStatus(PaymentStatus.PENDING); //put in pending for this moment

        PaymentResultDTO result = paymentGateway.paymentProcess(payment); //call the paymentGateway, using strategy design pattern

        //change the status according to logic
        if (result.success()){
            payment.setStatus(PaymentStatus.APPROVED);
        }else{
            payment.setStatus(PaymentStatus.REJECTED);
        }

        return result;
    }

}
