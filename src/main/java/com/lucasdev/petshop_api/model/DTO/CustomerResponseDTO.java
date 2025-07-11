package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Customer;

public record CustomerResponseDTO(Long id, String name, String email, String phone, String cpf) {

    public CustomerResponseDTO(Customer entityRef){
        this(entityRef.getId(), entityRef.getName(), entityRef.getEmail(), entityRef.getPhone(), entityRef.getCpf());
    }
}

//This dto represents the responses than i´ll send to front