package com.guideme.ms3_reservations_payments.dto;

import com.guideme.ms3_reservations_payments.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;

public record PaiementRequest(
        @NotNull Long idReservation,
        @NotNull ModePaiement mode
) {
}
