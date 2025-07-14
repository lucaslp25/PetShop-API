package com.lucasdev.petshop_api.security.model.DTO;

import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.model.UserRole;

public record RegisterResponseDTO(String username, String password, UserRole role, String message) {

    public RegisterResponseDTO(User userEntity, String message){
        this(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                message); //the message will guide de username
    }
}
