package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Customer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerUpdateDTO(


        String name,

        @Email(message = "Invalid email format.")
        String email,

        @Pattern(regexp = "(^\\d{2}\\d{9}$)|(^\\d{2}\\d{8}$)", message = "Invalid phone format.")
        String phone
) {

    public CustomerUpdateDTO(Customer entity){
        this(entity.getName(), entity.getEmail(), entity.getPhone());
    }

}