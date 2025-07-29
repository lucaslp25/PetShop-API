package com.lucasdev.petshop_api.model.DTO;

public record LoginResponseDTO(String token, String name, String email, String role) {
}
