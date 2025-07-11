package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.PetCreateDTO;
import com.lucasdev.petshop_api.model.DTO.PetResponseDTO;
import com.lucasdev.petshop_api.model.DTO.PetUpdateDTO;
import com.lucasdev.petshop_api.services.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PetResponseDTO> findById(@PathVariable Long id) {

        PetResponseDTO dto = service.findById(id);

        return ResponseEntity.ok().body(dto); //code 200
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> findAll() {

        List<PetResponseDTO> dtos = service.findAll();

        return ResponseEntity.ok().body(dtos); //code 200
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> insert(@Valid @RequestBody PetCreateDTO dtoRef) {

        PetResponseDTO dto = service.insert(dtoRef);

        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(dto.id()).
                toUri();

        return ResponseEntity.created(uri).body(dto); //code 201
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<PetResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PetUpdateDTO dtoRef) {

        PetResponseDTO dto = service.update(id, dtoRef);

        return ResponseEntity.ok().body(dto); //code 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build(); //code 204
    }

}