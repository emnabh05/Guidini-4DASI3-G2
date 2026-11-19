package com.guideme.ms3_reservations_payments.dto;

import com.guideme.ms3_reservations_payments.enums.TypeReservation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationRequest(
        @NotNull Long idClient,
        @NotNull TypeReservation typeReservation,
        Long idActivite,
        Long idOffre,
        @NotNull @Min(1) Integer nombrePlaces,
        @NotEmpty List<String> participants
) {
}
