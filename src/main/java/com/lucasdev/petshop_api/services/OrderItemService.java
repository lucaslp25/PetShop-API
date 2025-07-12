package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.OrderItemResponseDTO;
import com.lucasdev.petshop_api.model.entities.OrderItem;
import com.lucasdev.petshop_api.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository, ProductService productService) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public OrderItemResponseDTO findById(Long id) {

        OrderItem orderItem = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order Item Not Found with id: " + id));

        return new OrderItemResponseDTO(orderItem);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponseDTO> findAll(){

        List<OrderItem> items = repository.findAll();

        return items.stream().map( OrderItemResponseDTO::new).collect(Collectors.toList());
    }


}