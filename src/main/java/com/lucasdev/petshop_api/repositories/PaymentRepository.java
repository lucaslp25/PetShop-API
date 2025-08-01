package com.lucasdev.petshop_api.repositories;

import com.lucasdev.petshop_api.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findBySaleId(Long saleId);
}
