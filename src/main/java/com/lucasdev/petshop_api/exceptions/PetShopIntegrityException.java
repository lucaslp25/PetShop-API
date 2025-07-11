package com.lucasdev.petshop_api.exceptions;

public class PetShopIntegrityException extends RuntimeException {

  private static final long serialVersionUID = 1L;

    public PetShopIntegrityException(String message) {
        super(message);
    }
}
