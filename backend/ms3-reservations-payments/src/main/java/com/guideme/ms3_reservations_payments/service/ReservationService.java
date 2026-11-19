package com.guideme.ms3_reservations_payments.service;

import com.guideme.ms3_reservations_payments.dto.ParticipantsRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationResponse;
import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.enums.TypeReservation;
import com.guideme.ms3_reservations_payments.exception.BadRequestException;
import com.guideme.ms3_reservations_payments.exception.ConflictException;
import com.guideme.ms3_reservations_payments.exception.ResourceNotFoundException;
import com.guideme.ms3_reservations_payments.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Value("${ms3.prix-unitaire-defaut:50}")
    private BigDecimal prixUnitaireDefaut;

    @Transactional
    public ReservationResponse creerReservation(ReservationRequest req) {
        verifierCible(req);
        verifierParticipants(req.participants(), req.nombrePlaces());

        Reservation reservation = Reservation.builder()
                .idClient(req.idClient())
                .typeReservation(req.typeReservation())
                .idActivite(req.idActivite())
                .idOffre(req.idOffre())
                .nombrePlaces(req.nombrePlaces())
                .participants(new ArrayList<>(req.participants()))
                .montantTotal(calculerMontant(prixUnitaireDefaut, req.nombrePlaces()))
                .statut(StatutReservation.EN_ATTENTE_PAIEMENT)
                .build();

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long id) {
        return ReservationResponse.from(trouver(id));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsClient(Long idClient) {
        List<Reservation> reservations = reservationRepository.findByIdClient(idClient);
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("Aucune réservation pour le client " + idClient);
        }
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    @Transactional
    public ReservationResponse modifierParticipants(Long id, ParticipantsRequest req) {
        Reservation reservation = trouver(id);
        if (reservation.getStatut() == StatutReservation.ANNULEE
                || reservation.getStatut() == StatutReservation.EXPIREE) {
            throw new ConflictException("Impossible de modifier une réservation " + reservation.getStatut());
        }
        verifierParticipants(req.participants(), reservation.getNombrePlaces());
        reservation.getParticipants().clear();
        reservation.getParticipants().addAll(req.participants());
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    public BigDecimal calculerMontant(BigDecimal prixUnitaire, int nombrePlaces) {
        return prixUnitaire.multiply(BigDecimal.valueOf(nombrePlaces));
    }

    Reservation trouver(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation " + id + " introuvable"));
    }

    private void verifierCible(ReservationRequest req) {
        if (req.typeReservation() == TypeReservation.ACTIVITE && req.idActivite() == null) {
            throw new BadRequestException("idActivite est obligatoire pour une réservation d'activité");
        }
        if (req.typeReservation() == TypeReservation.OFFRE && req.idOffre() == null) {
            throw new BadRequestException("idOffre est obligatoire pour une réservation d'offre");
        }
    }

    private void verifierParticipants(List<String> participants, int nombrePlaces) {
        if (participants.size() != nombrePlaces) {
            throw new BadRequestException("Le nombre de participants (" + participants.size()
                    + ") doit être égal au nombre de places (" + nombrePlaces + ")");
        }
    }
}