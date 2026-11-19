package com.guideme.ms3_reservations_payments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpirationScheduler {

    private final ReservationService reservationService;

    @Scheduled(fixedRate = 60000)
    public void expirerReservations() {
        int nombre = reservationService.expirerReservationsNonPayees();
        if (nombre > 0) {
            log.info("{} réservation(s) non payée(s) expirée(s)", nombre);
        }
    }
}