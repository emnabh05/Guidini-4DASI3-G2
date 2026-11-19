package com.guideme.ms3_reservations_payments.dto;

import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.enums.TypeReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
        Long idReservation,
        Long idClient,
        TypeReservation typeReservation,
        Long idActivite,
        Long idOffre,
        Integer nombrePlaces,
        List<String> participants,
        BigDecimal montantTotal,
        LocalDateTime dateReservation,
        StatutReservation statut
) {
    public static ReservationResponse from(Reservation r) {
        return new ReservationResponse(
                r.getIdReservation(), r.getIdClient(), r.getTypeReservation(),
                r.getIdActivite(), r.getIdOffre(), r.getNombrePlaces(),
                List.copyOf(r.getParticipants()), r.getMontantTotal(),
                r.getDateReservation(), r.getStatut());
    }
}
