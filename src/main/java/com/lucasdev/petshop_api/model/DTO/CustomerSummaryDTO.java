package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Customer;

public record CustomerSummaryDTO(Long id, String name, String cpf) {

    public CustomerSummaryDTO(Customer entityRef){
        this(entityRef.getId(), entityRef.getName(), entityRef.getCpf());
    }
}