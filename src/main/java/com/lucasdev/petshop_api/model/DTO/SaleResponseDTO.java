package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Sale;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record SaleResponseDTO(

        Long id,
        Instant date,
        BigDecimal total_price,
        PaymentResponseDTO payment,
        CustomerInfoDTO customer,
        EmployeeInfoDTO employee,
        List<OrderItemResponseDTO> products,
        List<PetShopOrderServiceResponseDTO> petShop_services,
        String paymentUrl
) {

    public SaleResponseDTO(Sale entity, String paymentUrl) {

        this(
                entity.getId(),
                entity.getDate(),
                entity.getTotalAmount(),

                new PaymentResponseDTO(entity.getPayment()),
                // this is possible because have bidirectional relation in USER/CUSTOMER!!
                new CustomerInfoDTO(entity.getCustomer().getCustomerProfile()),
                new EmployeeInfoDTO(entity.getEmployee().getEmployeeProfile()),

                entity.getOrderItems().stream().
                        map(OrderItemResponseDTO::new).
                        collect(Collectors.toList()),

                entity.getServiceOrders().stream().
                        map(PetShopOrderServiceResponseDTO::new).
                        collect(Collectors.toList()), paymentUrl
                );
    }

    public SaleResponseDTO(Sale entity){

        this(
                entity.getId(),
                entity.getDate(),
                entity.getTotalAmount(),

                new PaymentResponseDTO(entity.getPayment()),

                new CustomerInfoDTO(entity.getCustomer().getCustomerProfile()),
                new EmployeeInfoDTO(entity.getEmployee().getEmployeeProfile()),

                entity.getOrderItems().stream().
                        map(OrderItemResponseDTO::new).
                        collect(Collectors.toList()),

                entity.getServiceOrders().stream().
                        map(PetShopOrderServiceResponseDTO::new).
                        collect(Collectors.toList()), null
        );

    }
}