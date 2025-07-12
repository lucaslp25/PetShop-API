package com.lucasdev.petshop_api.exceptions;

public class ExceptionUnauthorizedPayment extends RuntimeException {

  private static final long serialVersionUID = 1L;

    public ExceptionUnauthorizedPayment(String message) {
        super(message);
    }
}
