package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.CustomerCreateDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerResponseDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerSummaryDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerUpdateDTO;
import com.lucasdev.petshop_api.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable Long id) {

        CustomerResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto); //code 200
    }

    @GetMapping
    public ResponseEntity<List<CustomerSummaryDTO>> findAll(){

        List<CustomerSummaryDTO> dtos = service.findAll();
        return ResponseEntity.ok().body(dtos); //code 200
    }

    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<CustomerResponseDTO> findByCpf(@PathVariable String cpf){

        CustomerResponseDTO dto = service.findByCpf(cpf);
        return ResponseEntity.ok().body(dto); //code 200
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> insert(@Valid @RequestBody CustomerCreateDTO dtoRef){

        CustomerResponseDTO dto = service.insert(dtoRef);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto); // code 201
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO dtoRef){

        CustomerResponseDTO dto = service.update(id, dtoRef);
        return ResponseEntity.ok().body(dto); //code 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.deleteById(id);
        return ResponseEntity.noContent().build(); //code 204
    }

}