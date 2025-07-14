package com.lucasdev.petshop_api.model.entities;

import com.lucasdev.petshop_api.security.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format.")
    private String email;

    @Pattern(regexp = "(^\\d{2}\\d{9}$)|(^\\d{2}\\d{8}$)", message = "Invalid phone format.")
    private String phone;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "The field 'cpf' cannot be empty.")
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)", message = "Invalid CPF format.")
    private String cpf;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    private User user;

    public Employee(Long id, String name, String email, String phone, String cpf) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
    }
}
