package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.enums.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleCreateDTO(

        @NotBlank
        String customerId,

        @NotBlank
        String employeeId,

        Long petId,

        List<CreateOrderItemDTO> productsItems,
        // with this product can make the role of OrderService, then we´ll delete de orderService in future

        List<CreatePetShopServiceDTO> serviceItems,

        @NotNull(message = "The sale must have an paymentMode")
        PaymentMode paymentMode
){

}
