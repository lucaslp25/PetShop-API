package com.lucasdev.petshop_api.services;

import com.lucasdev.petshop_api.exceptions.PetShopIntegrityException;
import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.EmployeeCreateDTO;
import com.lucasdev.petshop_api.model.DTO.EmployeeResponseDTO;
import com.lucasdev.petshop_api.model.DTO.EmployeeUpdateDTO;
import com.lucasdev.petshop_api.model.entities.Employee;
import com.lucasdev.petshop_api.repositories.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(Long id){

        Employee entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee with id " + id + " not found."));

        return new EmployeeResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findByCpf(String cpf){

        Employee entity = repository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("employee with CPF " + cpf + " not found."));

        return new EmployeeResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findAll(){

        List<Employee> entities = repository.findAll();

        return entities.stream().map(EmployeeResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponseDTO insert(EmployeeCreateDTO dtoRef){

        if (repository.existsByCpf(dtoRef.cpf()) ||
                repository.existsByEmail(dtoRef.email())) {
            throw new PetShopIntegrityException("Already have an employee if this credentials.");
        }

        Employee entity = new Employee();

        BeanUtils.copyProperties(dtoRef, entity);

        entity = repository.save(entity);

        return new EmployeeResponseDTO(entity);
    }

    @Transactional
    public EmployeeResponseDTO update(Long id, EmployeeUpdateDTO dtoRef){

        Employee entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee with id " + id + " not found."));

        if (dtoRef.name() != null){
            entity.setName(dtoRef.name());
        }

        if (dtoRef.email() != null){
            entity.setEmail(dtoRef.email());
        }

        if (dtoRef.phone() != null){
            entity.setPhone(dtoRef.phone());
        }

        return new EmployeeResponseDTO(entity);
    }

    @Transactional
    public void deleteById(Long id){

        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("employee with id " + id + " not found.");
        }
        repository.deleteById(id);
    }
}
