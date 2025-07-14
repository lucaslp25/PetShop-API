package com.lucasdev.petshop_api.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PetShopOrderService implements Serializable {


    private static final long serialVerionUID = 1L;

    private Long id;

    private Pet pet;

    private Customer customer;

    private List<PetShopService> services;

    private BigDecimal totalPrice;


}
