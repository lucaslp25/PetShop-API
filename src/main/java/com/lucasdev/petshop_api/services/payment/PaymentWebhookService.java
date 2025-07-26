package com.lucasdev.petshop_api.services.payment;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.entities.OrderItem;
import com.lucasdev.petshop_api.model.entities.Payment;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.PaymentRepository;
import com.lucasdev.petshop_api.services.ProductService;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.merchantorder.MerchantOrderPayment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class PaymentWebhookService {

    private final PaymentRepository paymentRepository;
    private final ProductService productService;

    private static final Logger logger = Logger.getLogger(PaymentWebhookService.class.getName());

    public PaymentWebhookService(PaymentRepository paymentRepository, ProductService productService) {
        this.paymentRepository = paymentRepository;
        this.productService = productService;
    }

    @Transactional
    public void processMerchantOrderNotification(String merchantOrderId) {
        try {
            logger.info("Searching details of MerchantOrder ID: " + merchantOrderId);
            MerchantOrderClient client = new MerchantOrderClient();
            MerchantOrder merchantOrder = client.get(Long.parseLong(merchantOrderId));

            for (MerchantOrderPayment paymentResult : merchantOrder.getPayments()) {

                if ("approved".equals(paymentResult.getStatus())) {
                    logger.info("Payment ID " + paymentResult.getId() + " found into the MerchantOrder with status 'approved'.");

                    //if found an approved payment, we use it in parameters of the other method
                    this.processPaymentNotification(String.valueOf(paymentResult.getId()));
                } else {
                    logger.info("payment ID " + paymentResult.getId() + " found with status: '" + paymentResult.getStatus() + "'. Ignoring.");
                }
            }

        } catch (MPException | MPApiException e) {
            logger.log(Level.SEVERE, "Error in search details of MerchantOrder ID: " + merchantOrderId, e);
        }
    }

    //this method will happen if the above method really found a payment with approved status
    @Transactional
    public void processPaymentNotification(String gatewayPaymentId) {
        logger.info("Processing notification for Payment gateway ID: " + gatewayPaymentId);
        try {

            //Searching MP data with the ID that was in parameters, becase this have the above method for find this ID
            PaymentClient client = new PaymentClient();

            //good practice search direct on the MP Lib.
            com.mercadopago.resources.payment.Payment mpPayment = client.get(Long.parseLong(gatewayPaymentId));


            //now we use the external-reference for compare in database and find the correct payment for make the logic
            String saleId = mpPayment.getExternalReference();
            if (saleId == null) {
                logger.warning("Webhook received for payment without external_reference: " + gatewayPaymentId); //without the reference don´t have how do this
                return;
            }

            Payment paymentDb = paymentRepository.findBySaleId(Long.parseLong(saleId))
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + saleId));

            //seeing if the payment already has processed
            //IDEMPOTENCIA ... GOOOD PRACTICE
            if (paymentDb.getStatus() != PaymentStatus.PENDING) {
                logger.info("Payment " + paymentDb.getId() + " already has been processed. Status: " + paymentDb.getStatus());
                return;
            }
            //the mp will say what is the payment status
            String status = mpPayment.getStatus();

            logger.info("Updating payment " + paymentDb.getId() + " for MP status: " + status);

            //now is the real logic, depending on the status, decrease the DB and etc
            if ("approved".equals(status)) {
                paymentDb.setStatus(PaymentStatus.APPROVED);

                try{
                    for (OrderItem order : paymentDb.getSale().getOrderItems()){

                        productService.decreaseStock(order.getProduct().getId(), order.getQuantity());
                    }
                    logger.info("Stock has successfully updated for all items in sale ID:" + paymentDb.getSale().getId());
                }catch (Exception e){
                    //here is very important be attentive, because if fails, the payment will be approved but the stock not will be updated
                      logger.log(Level.SEVERE,"Error decreasing stock for payment " + paymentDb.getId()
                    );
                }

            } else if ("rejected".equals(status) || "cancelled".equals(status) || "refunded".equals(status)) {
                paymentDb.setStatus(PaymentStatus.REJECTED);
                // apply future logic to reverse the stock decrease in case of payment error
            }

            paymentRepository.save(paymentDb);
            logger.info("Payment " + paymentDb.getId() + " saved with new status: " + paymentDb.getStatus());

        } catch (MPException | MPApiException e) {
            logger.log(Level.SEVERE, "Error processing Mercado Pago notification: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error processing internal webhook logic: " + e.getMessage(), e);
        }
    }
}