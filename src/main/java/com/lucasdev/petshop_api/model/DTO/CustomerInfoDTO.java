package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Customer;


public record CustomerInfoDTO(String name, String email, String cpf) {

    public CustomerInfoDTO(Customer entity){
        this(entity.getName(), entity.getEmail(), entity.getCpf());
    }

}
