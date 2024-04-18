package com.cloud.backend.entities;

import com.cloud.backend.enumerations.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long subscriptionId;

    private Long amount;

    private String paymentIntentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
