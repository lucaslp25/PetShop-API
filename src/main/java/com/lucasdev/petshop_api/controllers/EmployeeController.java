package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.EmployeeCreateDTO;
import com.lucasdev.petshop_api.model.DTO.EmployeeResponseDTO;
import com.lucasdev.petshop_api.model.DTO.EmployeeUpdateDTO;
import com.lucasdev.petshop_api.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable Long id){

        EmployeeResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto); //code 200
    }

    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<EmployeeResponseDTO> findByCpf(@PathVariable String cpf){

        EmployeeResponseDTO dto = service.findByCpf(cpf);
        return ResponseEntity.ok().body(dto); //code 200
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> findAll(){

        List<EmployeeResponseDTO> dto = service.findAll();

        return ResponseEntity.ok().body(dto); //code 200
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> insert(@Valid @RequestBody EmployeeCreateDTO dtoRef){

        EmployeeResponseDTO dto = service.insert(dtoRef);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto); //code 201
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateDTO dtoRef){

        EmployeeResponseDTO dto = service.update(id, dtoRef);

        return ResponseEntity.ok().body(dto); //code 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        service.deleteById(id);

        return ResponseEntity.noContent().build(); //code 204
    }

}