package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.SaleCreateDTO;
import com.lucasdev.petshop_api.model.DTO.SaleResponseDTO;
import com.lucasdev.petshop_api.services.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sales")
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable Long id){

        SaleResponseDTO dto = service.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll(){

            List<SaleResponseDTO> dtos = service.findAll();
            return ResponseEntity.ok().body(dtos);
    }

}
