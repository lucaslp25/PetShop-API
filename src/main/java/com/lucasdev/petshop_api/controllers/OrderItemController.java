package com.lucasdev.petshop_api.controllers;

import com.lucasdev.petshop_api.model.DTO.OrderItemResponseDTO;
import com.lucasdev.petshop_api.services.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/orderItems")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> findAll() {

        List<OrderItemResponseDTO> dtos = service.findAll();
        return ResponseEntity.ok().body(dtos); //code 200
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderItemResponseDTO> findById(@PathVariable Long id) {

        OrderItemResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto); //code 200
    }
}

