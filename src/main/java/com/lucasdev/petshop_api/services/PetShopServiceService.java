package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.PetShopServiceResponseDTO;
import com.lucasdev.petshop_api.model.entities.PetShopService;
import com.lucasdev.petshop_api.repositories.PetShopServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetShopServiceService {

    private final PetShopServiceRepository repository;

    public PetShopServiceService(PetShopServiceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PetShopServiceResponseDTO findById(Long id){

        PetShopService entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        return new PetShopServiceResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<PetShopServiceResponseDTO> findAll(){

        List<PetShopService> entities = repository.findAll();

        return entities.stream().map(PetShopServiceResponseDTO::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    protected PetShopService findEntityById(Long id){

        PetShopService entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        return entity;
    }

}
