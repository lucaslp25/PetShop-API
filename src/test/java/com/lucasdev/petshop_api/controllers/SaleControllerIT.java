package com.lucasdev.petshop_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasdev.petshop_api.model.DTO.CreateOrderItemDTO;
import com.lucasdev.petshop_api.model.DTO.SaleCreateDTO;
import com.lucasdev.petshop_api.model.enums.PaymentMode;
import com.lucasdev.petshop_api.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
@Transactional
class SaleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should test all program logic when have products, and should update the stock")
    @WithMockUser(value = "bob.employee", roles = "EMPLOYEE")
    void shouldUpdateStock() throws Exception {

        SaleCreateDTO dtoRef = new SaleCreateDTO(
                "lucas.customer",
                "bob.employee",
                null,
                List.of(new CreateOrderItemDTO(2L, 10)),
                null,
                PaymentMode.PIX
        );

        String json = objectMapper.writeValueAsString(dtoRef);

        mockMvc.perform(post("/sales").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.customer.name").value("Lucas")).andExpect(jsonPath("$.payment.paymentMode").value("PIX")).andExpect(jsonPath("$.total_price").value(355.00));

        var product = productRepository.findById(2L).get();

        Assertions.assertEquals(product.getStock(), 90);
    }

}