package com.lucasdev.petshop_api.controllers.handler;

import com.lucasdev.petshop_api.exceptions.PetShopIntegrityException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
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
        String name = "Pet shop integrity error";
        String message = ex.getMessage();
        String path = request.getRequestURI();

        StandardError err = new StandardError(name, message, status, Instant.now(), path);
        return new ResponseEntity<>(err, status);
    }

}
