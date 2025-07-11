package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Employee;

public record EmployeeResponseDTO(Long id, String name, String email, String phone, String cpf)
{
    public EmployeeResponseDTO(Employee entity){
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getCpf());
    }
}


//id, name, email, phone, cpf