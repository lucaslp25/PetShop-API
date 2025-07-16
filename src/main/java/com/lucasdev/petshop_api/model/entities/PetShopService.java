package com.lucasdev.petshop_api.model.entities;

import com.lucasdev.petshop_api.model.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_pet_service")
public class PetShopService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Positive(message = "The field 'price' must be positive.")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToMany(mappedBy = "services")
    private List<PetShopOrderService> serviceOrders = new ArrayList<>();

    public PetShopService(Long id, String name, ServiceType serviceType, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.price = price;
    }
}