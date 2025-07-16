package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Employee;

public record EmployeeInfoDTO(String name) {

    public EmployeeInfoDTO(Employee entity){
        this(entity.getName());
    }

}
