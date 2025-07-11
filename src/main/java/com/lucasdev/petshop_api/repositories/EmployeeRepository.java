package com.lucasdev.petshop_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucasdev.petshop_api.model.entities.Employee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}