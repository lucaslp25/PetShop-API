package com.lucasdev.petshop_api.security.exceptions;

import com.lucasdev.petshop_api.exceptions.PetShopException;

public class PetShopLoginException extends PetShopException {

  private static final long serialVersionUID = 1L;

    public PetShopLoginException(String message) {
        super(message);
    }
}
