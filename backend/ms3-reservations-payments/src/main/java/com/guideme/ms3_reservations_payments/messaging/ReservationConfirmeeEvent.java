package com.guideme.ms3_reservations_payments.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationConfirmeeEvent(
        Long idReservation,
        Long idClient,
        Integer nombrePlaces,
        BigDecimal montantTotal,
        LocalDateTime dateConfirmation
) {
}