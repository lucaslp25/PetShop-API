package com.lucasdev.petshop_api.exceptions;

public class PetShopException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PetShopException(String message) {
        super(message);
    }
}
