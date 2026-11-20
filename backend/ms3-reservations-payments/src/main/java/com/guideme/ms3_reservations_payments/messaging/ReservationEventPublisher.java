package com.guideme.ms3_reservations_payments.messaging;

import com.guideme.ms3_reservations_payments.config.RabbitConfig;
import com.guideme.ms3_reservations_payments.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publierReservationConfirmee(Reservation reservation) {
        ReservationConfirmeeEvent event = new ReservationConfirmeeEvent(
                reservation.getIdReservation(),
                reservation.getIdClient(),
                reservation.getNombrePlaces(),
                reservation.getMontantTotal(),
                LocalDateTime.now());
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY_CONFIRMEE, event);
            log.info("Événement publié : réservation {} confirmée", reservation.getIdReservation());
        } catch (AmqpException e) {
            log.error("RabbitMQ indisponible, événement non publié pour la réservation {}",
                    reservation.getIdReservation(), e);
        }
    }
}