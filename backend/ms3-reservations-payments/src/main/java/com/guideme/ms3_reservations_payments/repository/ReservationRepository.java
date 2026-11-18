package com.guideme.ms3_reservations_payments.repository;

import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByIdClient(Long idClient);

    List<Reservation> findByStatutAndDateReservationBefore(StatutReservation statut, LocalDateTime date);
}
