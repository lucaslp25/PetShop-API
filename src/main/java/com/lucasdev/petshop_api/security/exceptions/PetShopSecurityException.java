package com.lucasdev.petshop_api.security.exceptions;

import com.lucasdev.petshop_api.exceptions.PetShopException;

public class PetShopSecurityException extends PetShopException {

    private static final long serialVersionUID = 1L;

    public PetShopSecurityException(String message) {
        super(message);
    }

}