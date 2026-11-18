package com.guideme.ms3_reservations_payments.repository;

import com.guideme.ms3_reservations_payments.entity.Paiement;
import com.guideme.ms3_reservations_payments.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByIdReservation(Long idReservation);

    boolean existsByIdReservationAndStatut(Long idReservation, StatutPaiement statut);
}
