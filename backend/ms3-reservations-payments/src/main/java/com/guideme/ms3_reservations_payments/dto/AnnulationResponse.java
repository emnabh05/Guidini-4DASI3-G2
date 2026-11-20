package com.guideme.ms3_reservations_payments.dto;

import com.guideme.ms3_reservations_payments.enums.StatutReservation;

import java.math.BigDecimal;

public record AnnulationResponse(
        Long idReservation,
        StatutReservation statut,
        int pourcentageRembourse,
        BigDecimal montantRembourse
) {
}