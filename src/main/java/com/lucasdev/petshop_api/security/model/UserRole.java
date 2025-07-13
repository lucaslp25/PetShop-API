package com.lucasdev.petshop_api.security.model;

public enum UserRole {

    EMPLOYEE("employee"),
    CUSTOMER("customer");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}