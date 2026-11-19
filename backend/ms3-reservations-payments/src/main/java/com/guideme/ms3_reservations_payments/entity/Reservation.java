package com.guideme.ms3_reservations_payments.entity;

import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.enums.TypeReservation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private Long idClient;

    @Enumerated(EnumType.STRING)
    private TypeReservation typeReservation;

    private Long idActivite;

    private Long idOffre;

    private Integer nombrePlaces;

    @ElementCollection
    @CollectionTable(name = "reservation_participants",
            joinColumns = @JoinColumn(name = "id_reservation"))
    @Column(name = "participant")
    @Builder.Default
    private List<String> participants = new ArrayList<>();

    private BigDecimal montantTotal;

    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    public void confirmer() {
        this.statut = StatutReservation.CONFIRMEE;
    }

    public void annuler() {
        this.statut = StatutReservation.ANNULEE;
    }

    public void expirer() {
        this.statut = StatutReservation.EXPIREE;
    }

    @PrePersist
    void avantCreation() {
        if (dateReservation == null) {
            dateReservation = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutReservation.EN_ATTENTE_PAIEMENT;
        }
    }
}