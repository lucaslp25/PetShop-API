package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.CreateOrderDTO;
import com.lucasdev.petshop_api.model.DTO.CreateOrderItemDTO;
import com.lucasdev.petshop_api.model.DTO.OrderItemResponseDTO;
import com.lucasdev.petshop_api.model.DTO.OrderResponseDTO;

import com.lucasdev.petshop_api.model.entities.*;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import com.lucasdev.petshop_api.repositories.OrderRepository;
import com.lucasdev.petshop_api.services.payment.PaymentResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final PaymentService paymentService;

    @Autowired
    public OrderService(OrderRepository repository, CustomerService customerService, ProductService productService, PaymentService paymentService) {
        this.repository = repository;
        this.customerService = customerService;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderDTO order){

        //this method make all logic of the order

        Customer customer = customerService.findEntityById(order.customer_id());

        Order orderEntity = new Order();

        BigDecimal totalPrice = BigDecimal.ZERO;

        Payment payment = new Payment();
        payment.setDate(Instant.now());
        payment.setPaymentMode(order.payment_mode());
        payment.setStatus(PaymentStatus.PENDING);
        orderEntity.setPayment(payment);

        PaymentResultDTO paymentResult = paymentService.executePaymentForOrder(orderEntity);


        List<OrderItem> entityOrdersItems = new ArrayList<>();


        for (CreateOrderItemDTO items : order.order_items()){

            Product product = productService.findEntityById(items.product_id());

            if (items.quantity() > product.getStock()){
                throw new RuntimeException("Insufficient stock");
            }

            if (paymentResult.success()) {
                product.setStock(product.getStock() - items.quantity());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(items.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setOrder(orderEntity);

            entityOrdersItems.add(orderItem);

            orderEntity.getItems().add(orderItem);

            totalPrice = totalPrice.add(orderItem.getUnitPrice().multiply(new BigDecimal(items.quantity())));
        }

        List<OrderItemResponseDTO> ordersItems = entityOrdersItems.stream().map(OrderItemResponseDTO::new).collect(Collectors.toList());

        if (!paymentResult.success()){
            payment.setStatus(PaymentStatus.REJECTED);
        }
        else {
            payment.setStatus(PaymentStatus.APPROVED);
        }
        payment.setAmount(totalPrice);

        orderEntity.setTotalPrice(totalPrice);

        Order finalOrder = repository.save(orderEntity);

        return new OrderResponseDTO(finalOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findById(Long id) {

        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return new OrderResponseDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAll() {

        List<Order> orders = repository.findAll();

        return orders.stream().map(OrderResponseDTO::new).collect(Collectors.toList());
    }


}