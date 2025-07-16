package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.model.DTO.*;
import com.lucasdev.petshop_api.model.entities.*;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.SaleRepository;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.services.UserService;
import com.lucasdev.petshop_api.services.payment.PaymentResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SaleService {

    private final SaleRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final PetShopServiceService petShopServiceService;
    private final PetService petService;
    private final PaymentService paymentService;


    public SaleService(SaleRepository repository, UserService userService, ProductService productService, PetShopServiceService petShopServiceService, PetService petService, PaymentService paymentService) {
        this.repository = repository;
        this.userService = userService;
        this.productService = productService;
        this.petShopServiceService = petShopServiceService;
        this.petService = petService;
        this.paymentService = paymentService;
    }

    @Transactional
    public SaleResponseDTO insertSale(SaleCreateDTO saleRef){

        boolean hasProducts = saleRef.productsItems() != null && !saleRef.productsItems().isEmpty();
        boolean hasServices = saleRef.serviceItems() != null && !saleRef.serviceItems().isEmpty();
        if (!hasProducts && !hasServices) {
            throw new IllegalArgumentException("A sale must have at least one product or service.");
        }

        User costumer = userService.findUserByLogin(saleRef.customerId());

        User employee = userService.findUserByLogin(saleRef.employeeId());

        Pet pet = petService.findEntityById(saleRef.petId());

        Sale saleEntity = new Sale();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CreateOrderItemDTO i: saleRef.productsItems()){

            Product product = productService.findEntityById(i.product_id());

            if (i.quantity() > 0 && i.quantity() <= product.getStock()){
                product.setStock(product.getStock() - i.quantity());
            }

            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(i.quantity())));


            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(i.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSale(saleEntity);

            saleEntity.getOrderItems().add(orderItem);
        }

        for (CreatePetShopServiceDTO j: saleRef.serviceItems()){

            PetShopService service = petShopServiceService.findEntityById(j.serviceId());

            PetShopOrderService orderService = new PetShopOrderService();

            orderService.setTotalPrice(service.getPrice());
            orderService.setCustomer(costumer.getCustomerProfile());
            orderService.setPet(pet);
            orderService.setSale(saleEntity);

            saleEntity.getServiceOrders().add(orderService);

            totalPrice = totalPrice.add(orderService.getTotalPrice());
        }

        Payment payment = new Payment();

        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(totalPrice);
        payment.setPaymentMode(saleRef.paymentMode());

        saleEntity.setPayment(payment);

        PaymentResultDTO payResult = paymentService.executePaymentForOrder(saleEntity);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(payment);

        saleEntity.setCustomer(costumer);
        saleEntity.setEmployee(employee);
        saleEntity.setTotalAmount(totalPrice);

        Sale finalSale = repository.save(saleEntity);

        return new SaleResponseDTO(finalSale);
    }

}