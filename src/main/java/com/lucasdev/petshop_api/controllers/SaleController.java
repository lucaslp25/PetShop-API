package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.SaleCreateDTO;
import com.lucasdev.petshop_api.model.DTO.SaleResponseDTO;
import com.lucasdev.petshop_api.services.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

    private final SaleService service;


    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> insertSale(@Valid @RequestBody SaleCreateDTO dtoRef){

        SaleResponseDTO dto = service.insertSale(dtoRef);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

}
