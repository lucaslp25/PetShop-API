package com.lucasdev.petshop_api.controllers.handler;

import com.lucasdev.petshop_api.exceptions.PetShopIntegrityException;
import com.lucasdev.petshop_api.exceptions.PetShopSaleException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.security.exceptions.PetShopLoginException;
import com.lucasdev.petshop_api.security.exceptions.PetShopSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String name = "Resource not found";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }

    @ExceptionHandler(PetShopIntegrityException.class)
    public ResponseEntity<StandardError> integrityPetShopException(PetShopIntegrityException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String name = "PetShop integrity error";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }


    @ExceptionHandler(PetShopSecurityException.class)
    public ResponseEntity<StandardError> petShopSecurityException(PetShopSecurityException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED; // code 401
        String name = "PetShop security exception!";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }

    @ExceptionHandler(PetShopLoginException.class)
    public ResponseEntity<StandardError> petShopSecurityException(PetShopLoginException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; // code 400
        String name = "PetShop Login exception!";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }


    @ExceptionHandler(PetShopSaleException.class)
    public ResponseEntity<StandardError> petShopSaleException(PetShopSaleException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // code 422
        String name = "PetShop Bussines Rules violation!";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }

}

