package com.lucasdev.petshop_api.services.payment;

import com.lucasdev.petshop_api.model.entities.OrderItem;
import com.lucasdev.petshop_api.model.entities.Payment;
import com.lucasdev.petshop_api.model.entities.PetShopOrderService;
import com.lucasdev.petshop_api.repositories.PaymentRepository;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary //using this annotation because the interface have more than one implementation, then we say to spring give preference to this
public class MercadoPagoPaymentGateway implements PaymentGateway {

    private final PaymentRepository paymentRepository;

    public MercadoPagoPaymentGateway(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PreferenceCreationResultDTO paymentProcess(Payment payment) {

        PreferenceClient client = new PreferenceClient();

        try {

            List<PreferenceItemRequest> items = new ArrayList<>();

            for (OrderItem orderItem : payment.getSale().getOrderItems()) { //add the main OrderItem attributes for items list

                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                        .id(orderItem.getProduct().getId().toString())
                        .title(orderItem.getProduct().getName())
                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getUnitPrice())
                        .build();

                items.add(itemRequest);
            }

            //add the Service attributes too!
            for (PetShopOrderService serviceOrder : payment.getSale().getServiceOrders()) {

                serviceOrder.getServices().forEach(service -> {
                    PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                            .id(service.getId().toString())
                            .title(service.getName())
                            .unitPrice(service.getPrice())
                            .quantity(1) //one service per order
                            .build();

                    items.add(itemRequest);
                });
            }

            //now we make the ways for each payment status result
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:8080/success") //way for success, fail, and pending
                    .failure("http://localhost:8080/failure")
                    .pending("http://localhost:8080/pending")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items) //add my list of items
                    .backUrls(backUrls) //add the backUrls
                    //create the link of MP Payment and my sale
                    .externalReference(payment.getSale().getId().toString())
                    .build();

            Preference preference = client.create(preferenceRequest);

            //after make´s the preference, we use it for get the initPoint, (link of payment), and the preference id
            String paymentUrl = preference.getInitPoint();
            String paymentId = preference.getId();

            //take the gatewayPayment id and put into the entity, and finally save in rep
            payment.setGatewayPaymentId(paymentId);
            paymentRepository.save(payment);

            // if the preference was created, we return true in PaymentResultDTO, together with the PaymentUrl and PaymentId
            return new PreferenceCreationResultDTO(true, paymentUrl, paymentId);

            //catch with the personalized MP erros
        } catch (MPApiException e) {

            System.err.println("Error of Mercado Pago API: " + e.getApiResponse().getContent());
            return PreferenceCreationResultDTO.error(e.getApiResponse().getContent()); //then return false in success
        } catch (MPException e) {
            // the MpException is the most generic of MpExceptions, then we use it for example in networking error
            e.printStackTrace();
            return PreferenceCreationResultDTO.error(e.getMessage()); //return false in the success too
        }
    }
}
