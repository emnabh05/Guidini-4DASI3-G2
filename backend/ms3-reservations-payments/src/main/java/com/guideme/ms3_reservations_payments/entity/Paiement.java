package com.guideme.ms3_reservations_payments.entity;

import com.guideme.ms3_reservations_payments.enums.ModePaiement;
import com.guideme.ms3_reservations_payments.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;

    @Column(nullable = false)
    private Long idReservation;

    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private ModePaiement mode;

    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    @PrePersist
    void avantCreation() {
        if (datePaiement == null) {
            datePaiement = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutPaiement.EN_ATTENTE;
        }
    }
}