package com.lucasdev.petshop_api.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_petshop_order_service")
public class PetShopOrderService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy mm:HH:ss'T'", timezone = "GMT")
    private Instant date;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "tb_petshop_order_items", //name of table
            joinColumns = // puting COLUMNS in a table
            @JoinColumn(name = "petshop_order_service_id", nullable = true), //the column of primary key
            inverseJoinColumns = @JoinColumn(name = "petshop_service_id") //the foreing key column
    )
    private List<PetShopService> services = new ArrayList<>();


   @PrePersist
    protected void onCreate() {
       this.date = Instant.now();
   }

}