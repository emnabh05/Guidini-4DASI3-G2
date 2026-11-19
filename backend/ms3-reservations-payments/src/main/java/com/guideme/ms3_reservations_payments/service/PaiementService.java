package com.guideme.ms3_reservations_payments.service;

import com.guideme.ms3_reservations_payments.dto.PaiementRequest;
import com.guideme.ms3_reservations_payments.dto.PaiementResponse;
import com.guideme.ms3_reservations_payments.entity.Paiement;
import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.StatutPaiement;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.exception.ConflictException;
import com.guideme.ms3_reservations_payments.exception.ResourceNotFoundException;
import com.guideme.ms3_reservations_payments.repository.PaiementRepository;
import com.guideme.ms3_reservations_payments.repository.ReservationRepository;
import com.guideme.ms3_reservations_payments.messaging.ReservationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationEventPublisher eventPublisher;

    @Transactional
    public PaiementResponse effectuerPaiement(PaiementRequest req) {
        Reservation reservation = reservationRepository.findById(req.idReservation())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation " + req.idReservation() + " introuvable"));

        if (reservation.getStatut() != StatutReservation.EN_ATTENTE_PAIEMENT) {
            throw new ConflictException("Cette réservation est " + reservation.getStatut()
                    + " : le paiement n'est possible que pour une réservation EN_ATTENTE_PAIEMENT");
        }
        if (paiementRepository.existsByIdReservationAndStatut(reservation.getIdReservation(),
                StatutPaiement.VALIDE)) {
            throw new ConflictException("Cette réservation est déjà payée");
        }

        Paiement paiement = Paiement.builder()
                .idReservation(reservation.getIdReservation())
                .montant(reservation.getMontantTotal())
                .mode(req.mode())
                .statut(StatutPaiement.VALIDE)
                .build();
        paiement = paiementRepository.save(paiement);

        reservation.confirmer();
        reservationRepository.save(reservation);
        eventPublisher.publierReservationConfirmee(reservation);

        return PaiementResponse.from(paiement);
    }

    @Transactional(readOnly = true)
    public PaiementResponse getPaiement(Long id) {
        return PaiementResponse.from(paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement " + id + " introuvable")));
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> getPaiementsReservation(Long idReservation) {
        List<Paiement> paiements = paiementRepository.findByIdReservation(idReservation);
        if (paiements.isEmpty()) {
            throw new ResourceNotFoundException("Aucun paiement pour la réservation " + idReservation);
        }
        return paiements.stream().map(PaiementResponse::from).toList();
    }
}