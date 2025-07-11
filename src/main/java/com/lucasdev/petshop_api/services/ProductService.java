package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.ProductCreateDTO;
import com.lucasdev.petshop_api.model.DTO.ProductResponseDTO;
import com.lucasdev.petshop_api.model.DTO.ProductUpdateDTO;
import com.lucasdev.petshop_api.model.entities.Product;
import com.lucasdev.petshop_api.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponseDTO insert(ProductCreateDTO dtoRef){

        Product entity = new Product();

        BeanUtils.copyProperties(dtoRef, entity);

        entity = repository.save(entity);

        return new ProductResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll(){

        List<Product> entities = repository.findAll();
        return entities.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id){

        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        return new ProductResponseDTO(entity);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductUpdateDTO dtoRef){

        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        BeanUtils.copyProperties(dtoRef, entity);

        return new ProductResponseDTO(entity);
    }

    @Transactional
    public void delete(Long id){

        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        repository.deleteById(id);
    }

    @Transactional
    public ProductResponseDTO updateStock(Long id, Integer stock){

        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        entity.setStock(entity.getStock() + stock);

        entity = repository.save(entity);

        return new ProductResponseDTO(entity);
    }

}