package com.lucasdev.petshop_api.repositories;

import com.lucasdev.petshop_api.model.entities.PetShopService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetShopServiceRepository extends JpaRepository<PetShopService, Long> {
}
