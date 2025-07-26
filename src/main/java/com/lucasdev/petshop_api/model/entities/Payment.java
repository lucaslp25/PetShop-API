package com.lucasdev.petshop_api.model.entities;

import com.lucasdev.petshop_api.model.enums.PaymentMode;
import com.lucasdev.petshop_api.model.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant date;

    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    private Sale sale;

    @Column(name = "gateway_payment_id") //need this for ExternalServices
    private String gatewayPaymentId;

    public Payment(Instant date, BigDecimal amount, PaymentMode paymentMode, PaymentStatus status, Sale sale, String gatewayPaymentId) {
        this.date = date;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.status = status;
        this.sale = sale;
        this.gatewayPaymentId = gatewayPaymentId;
    }
}