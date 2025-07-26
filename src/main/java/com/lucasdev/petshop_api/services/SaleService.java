package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.PetShopSaleException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.*;
import com.lucasdev.petshop_api.model.entities.*;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.SaleRepository;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.services.UserService;
import com.lucasdev.petshop_api.services.payment.PreferenceCreationResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new PetShopSaleException("A sale must have at least one product or service.");
        }

        User customer = userService.findUserByLogin(saleRef.customerId());

        User employee = userService.findUserByLogin(saleRef.employeeId());

        Sale saleEntity = new Sale();
        saleEntity.setCustomer(customer);
        saleEntity.setEmployee(employee);
        saleEntity.setDate(Instant.now());

        BigDecimal totalPrice = BigDecimal.ZERO;

        if (hasProducts) { //economy loops if don´t have any products in order!

            for (CreateOrderItemDTO i : saleRef.productsItems()) {

                Product product = productService.findEntityById(i.product_id());

                if (i.quantity() <= 0 || i.quantity() > product.getStock()) {
                    throw new PetShopSaleException("Invalid quantity or insufficient stock for: " + product.getName());
                }

                totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(i.quantity())));


                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(i.quantity());
                orderItem.setUnitPrice(product.getPrice());
                orderItem.setSale(saleEntity);

                saleEntity.getOrderItems().add(orderItem);
            }
        }

        if (hasServices) {

            Pet pet = petService.findEntityById(saleRef.petId());

            PetShopOrderService orderService = new PetShopOrderService();
            orderService.setPet(pet);

            orderService.setSale(saleEntity);

            BigDecimal servicesSubtotal = BigDecimal.ZERO;

            for (CreatePetShopServiceDTO j : saleRef.serviceItems()) {

                PetShopService service = petShopServiceService.findEntityById(j.serviceId());

                orderService.getServices().add(service);
                servicesSubtotal = servicesSubtotal.add(service.getPrice());
            }
            orderService.setTotalPrice(servicesSubtotal);
            saleEntity.getServiceOrders().add(orderService);

            totalPrice = totalPrice.add(orderService.getTotalPrice());
        }

        saleEntity.setTotalAmount(totalPrice);

        Payment payment = new Payment();
        payment.setDate(Instant.now());
        payment.setAmount(totalPrice);
        payment.setPaymentMode(saleRef.paymentMode());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSale(saleEntity);

        saleEntity.setPayment(payment);
        saleEntity = repository.save(saleEntity);

        PreferenceCreationResultDTO preferenceResult = paymentService.initPayment(saleEntity.getPayment());

        Sale finalSale = repository.save(saleEntity);

        if (!preferenceResult.preferenceCreated()){
            throw new PetShopSaleException("The sale has not been created.");
        }

        return new SaleResponseDTO(finalSale, preferenceResult.paymentUrl());
    }

    @Transactional(readOnly = true)
    public SaleResponseDTO findById(Long id){

        Sale entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found sale with id: " + id));

        return new SaleResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDTO> findAll() {

        List<Sale> entities = repository.findAll();

        return entities.stream().map(SaleResponseDTO::new).collect(Collectors.toList());
    }
}