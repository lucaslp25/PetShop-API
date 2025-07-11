package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.CustomerResponseDTO;
import com.lucasdev.petshop_api.model.DTO.PetCreateDTO;
import com.lucasdev.petshop_api.model.DTO.PetResponseDTO;
import com.lucasdev.petshop_api.model.DTO.PetUpdateDTO;
import com.lucasdev.petshop_api.model.entities.Customer;
import com.lucasdev.petshop_api.model.entities.Pet;
import com.lucasdev.petshop_api.repositories.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final CustomerService customerService;

    public PetService(PetRepository petRepository, CustomerService customerService) {
        this.petRepository = petRepository;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public PetResponseDTO findById(Long id) {

        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet with id " + id + " not found"));
        return new PetResponseDTO(pet);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> findAll() {

        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public PetResponseDTO insert(PetCreateDTO dtoRef){

        //better practice because the customerService already do validation and return if´s okay
        Customer owner = customerService.findEntityById(dtoRef.owner_id());

        Pet pet = Pet.builder().
                name(dtoRef.name())
                .type(dtoRef.type())
                .owner(owner).
                build();

        pet = petRepository.save(pet);

        return new PetResponseDTO(pet);
    }

    @Transactional
    public PetResponseDTO update(Long id, PetUpdateDTO dtoRef){

        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet with id " + id + " not found"));

        if (dtoRef.name() != null) {
            pet.setName(dtoRef.name());
        }

        return new PetResponseDTO(pet);
    }

    @Transactional
    public void delete(Long id) {

        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet with id " + id + " not found");
        }
        petRepository.deleteById(id);
    }



}
