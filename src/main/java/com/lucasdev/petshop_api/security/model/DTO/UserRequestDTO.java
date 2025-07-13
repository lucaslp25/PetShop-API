package com.lucasdev.petshop_api.security.model.DTO;

import com.lucasdev.petshop_api.security.model.UserRole;

public record UserRequestDTO(String username, String password, UserRole role) {
}
