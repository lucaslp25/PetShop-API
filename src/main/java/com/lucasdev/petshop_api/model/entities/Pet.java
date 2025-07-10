package com.lucasdev.petshop_api.model.entities;

import com.lucasdev.petshop_api.model.enums.PetType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private PetType type;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer owner;
}
