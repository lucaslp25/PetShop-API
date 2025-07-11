package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.ProductCreateDTO;
import com.lucasdev.petshop_api.model.DTO.ProductResponseDTO;
import com.lucasdev.petshop_api.model.DTO.ProductUpdateDTO;
import com.lucasdev.petshop_api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll(){

        List<ProductResponseDTO> dtos = productService.findAll();

        return ResponseEntity.ok().body(dtos);//code 200
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id){

        ProductResponseDTO dto = productService.findById(id);

        return ResponseEntity.ok().body(dto);//code 200
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> insert(@Valid @RequestBody ProductCreateDTO dtoRef){

        ProductResponseDTO dto = productService.insert(dtoRef);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping(value = "/{id}/stock")
    public ResponseEntity<ProductResponseDTO> increaseStock(@PathVariable Long id, Integer quantity){

        ProductResponseDTO dto = productService.updateStock(id, quantity);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto); //code201
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO dtoRef){

        ProductResponseDTO dto = productService.update(id, dtoRef);

        return ResponseEntity.ok().body(dto); //code 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        productService.delete(id);

        return ResponseEntity.noContent().build(); //code 204
    }
}