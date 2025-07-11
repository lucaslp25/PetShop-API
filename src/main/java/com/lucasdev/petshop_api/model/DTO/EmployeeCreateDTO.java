package com.lucasdev.petshop_api.model.DTO;

import com.lucasdev.petshop_api.model.entities.Employee;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmployeeCreateDTO(

        @Column(nullable = false)
        String name,

        @Column(unique = true, nullable = false)
        @Email(message = "Invalid email format.")
        String email,

        @Pattern(regexp = "(^\\d{2}\\d{9}$)|(^\\d{2}\\d{8}$)", message = "Invalid phone format.")
        String phone,

        @Column(unique = true, nullable = false)
        @NotBlank(message = "The field 'cpf' cannot be empty.")
        @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)", message = "Invalid CPF format.")
        String cpf
) {
    public EmployeeCreateDTO(Employee entity){
        this(entity.getName(), entity.getEmail(), entity.getPhone(), entity.getCpf());
    }

}