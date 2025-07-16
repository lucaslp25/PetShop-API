package com.lucasdev.petshop_api.security.model.DTO;

import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.model.UserRole;

public record UserResponseDTO(String login, UserRole userRole) {

    public UserResponseDTO(User entity){
        this(entity.getLogin(), entity.getRole());
    }
}
