package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.services.payment.PaymentWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/webhooks")
public class MercadoPagoWebhookController {

    private final PaymentWebhookService webhookService;

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoWebhookController.class);

    public MercadoPagoWebhookController(PaymentWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/mercado-pago")
    public ResponseEntity<Void> handleMercadoPagoWebHook(
            @RequestBody(required = false) Map<String, Object> payload,
            @RequestParam(required = false) Map<String, String> params) {

        logger.info("Mercado Pago webhook Received!");
        logger.info("Payload: " + payload);
        logger.info("Params: " + params);

        String notificationType = findNotificationType(payload, params);
        logger.info("Detection notification type: {} ", notificationType);

        //the main entry point ie the 'merchant-order' notification now
        if ("merchant_order".equals(notificationType)) {
            String merchantOrderId = findMerchantOrderId(payload, params);
            if (merchantOrderId != null) {
                logger.info("Extracted MerchantOrder ID: {} " , merchantOrderId + ". Processing...");
                webhookService.processMerchantOrderNotification(merchantOrderId);
            } else {
                logger.warn("Notification of type 'merchant_order' received, but the ID could not be extracted..");
            }
            //keeping the defensive old style
        } else if ("payment".equals(notificationType)) {

            String paymentId = findPaymentId(payload, params);
            if (paymentId != null) {
                logger.info("Direct 'payment' notification received. ID: {}. Processing...", paymentId);
                webhookService.processPaymentNotification(paymentId);
            }
        } else {
            //in other 'types' cases
            logger.info("Notification of type '{}' received and ignored.", notificationType);
        }

        return ResponseEntity.ok().build(); //code 200 ok
    }

    //auxiliar methods

    private String findMerchantOrderId(Map<String, Object> payload, Map<String, String> params) {

        if (params != null && params.get("data.id") != null) {
            logger.info("MerchantOrder ID found in URL parameters (data.id).");
            return params.get("data.id");
        }

        if (payload != null && payload.get("id") != null) {
            logger.debug("MerchantOrder ID found in payload's top-level 'id' field.");
            return String.valueOf(payload.get("id"));
        }

        if (payload != null && payload.get("data") instanceof Map) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data.get("id") != null) {
                logger.debug("MerchantOrder ID found inside the payload's 'data' object.");
                return String.valueOf(data.get("id"));
            }
        }

        logger.warn("Unable to extract MerchantOrder ID from any known source.");
        return null;
    }

    private String findNotificationType(Map<String, Object> payload, Map<String, String> params) {
        if (payload != null && payload.get("type") != null) {

            String type = (String) payload.get("type");
            return type.contains("merchant_order") ? "merchant_order" : type;
        }
        if (params != null && params.get("type") != null) {
            String type = params.get("type");
            return type.contains("merchant_order") ? "merchant_order" : type;
        }
        return "unknown";
    }

    private String findPaymentId(Map<String, Object> payload, Map<String, String> params) {
        if (payload != null) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data != null) {

                return String.valueOf(data.get("id"));
            }
        }
        if (params != null) {
            return params.get("data.id");
        }
        return null;
    }
}