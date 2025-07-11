package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.PetShopIntegrityException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.CustomerCreateDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerResponseDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerSummaryDTO;
import com.lucasdev.petshop_api.model.DTO.CustomerUpdateDTO;
import com.lucasdev.petshop_api.model.entities.Customer;
import com.lucasdev.petshop_api.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findById(Long id){

        Customer entity = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found."));

        return new CustomerResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findByCpf(String cpf){

        Customer entity = customerRepository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("Customer with CPF " + cpf + " not found."));

        return new CustomerResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryDTO> findAll(){

        List<Customer> entities = customerRepository.findAll();

        return entities.stream().map(CustomerSummaryDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponseDTO insert(CustomerCreateDTO dtoRef){

        if (customerRepository.existsByCpf(dtoRef.cpf()) ||
                customerRepository.existsByEmail(dtoRef.email())) {
            throw new PetShopIntegrityException("Already have an Customer if this credentials.");
        }

        Customer entity = new Customer();

        BeanUtils.copyProperties(dtoRef, entity);

        entity = customerRepository.save(entity);

        return new CustomerResponseDTO(entity);
    }

    @Transactional
    public CustomerResponseDTO update(Long id, CustomerUpdateDTO dtoRef){

        Customer entity = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found."));

        if (dtoRef.name() != null){
            entity.setName(dtoRef.name());
        }

        if (dtoRef.email() != null){
            entity.setEmail(dtoRef.email());
        }

        if (dtoRef.phone() != null){
            entity.setPhone(dtoRef.phone());
        }

        return new CustomerResponseDTO(entity);
    }

    @Transactional
    public void deleteById(Long id){

        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer with id " + id + " not found.");
        }
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    protected Customer findEntityById(Long id){

        Customer entity = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found."));

        return entity;
    }

}