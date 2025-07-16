package com.lucasdev.petshop_api.repositories;

import com.lucasdev.petshop_api.model.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
