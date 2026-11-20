package com.guideme.ms3_reservations_payments.service;

import com.guideme.ms3_reservations_payments.dto.PaiementRequest;
import com.guideme.ms3_reservations_payments.dto.PaiementResponse;
import com.guideme.ms3_reservations_payments.entity.Paiement;
import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.ModePaiement;
import com.guideme.ms3_reservations_payments.enums.StatutPaiement;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.enums.TypeReservation;
import com.guideme.ms3_reservations_payments.exception.ConflictException;
import com.guideme.ms3_reservations_payments.exception.ResourceNotFoundException;
import com.guideme.ms3_reservations_payments.messaging.ReservationEventPublisher;
import com.guideme.ms3_reservations_payments.repository.PaiementRepository;
import com.guideme.ms3_reservations_payments.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationEventPublisher eventPublisher;

    @InjectMocks
    private PaiementService service;

    private Reservation reservation(StatutReservation statut) {
        return Reservation.builder()
                .idReservation(1L)
                .idClient(1L)
                .typeReservation(TypeReservation.ACTIVITE)
                .idActivite(10L)
                .nombrePlaces(3)
                .participants(new ArrayList<>(List.of("Emna", "Sami", "Yasmine")))
                .montantTotal(new BigDecimal("150"))
                .dateReservation(LocalDateTime.now())
                .statut(statut)
                .build();
    }

    @Test
    void effectuerPaiement_reservationEnAttente_ValideConfirmeEtPublieEvenement() {
        Reservation r = reservation(StatutReservation.EN_ATTENTE_PAIEMENT);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(paiementRepository.existsByIdReservationAndStatut(1L, StatutPaiement.VALIDE)).thenReturn(false);
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(i -> i.getArgument(0));

        PaiementResponse rep = service.effectuerPaiement(new PaiementRequest(1L, ModePaiement.CARTE_BANCAIRE));

        assertThat(rep.montant()).isEqualByComparingTo("150");
        assertThat(rep.statut()).isEqualTo(StatutPaiement.VALIDE);
        assertThat(r.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
        verify(eventPublisher).publierReservationConfirmee(r);
    }

    @Test
    void effectuerPaiement_reservationInexistante_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.effectuerPaiement(new PaiementRequest(99L, ModePaiement.ESPECES)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void effectuerPaiement_reservationDejaConfirmee_Conflit() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation(StatutReservation.CONFIRMEE)));

        assertThatThrownBy(() -> service.effectuerPaiement(new PaiementRequest(1L, ModePaiement.ESPECES)))
                .isInstanceOf(ConflictException.class);
        verify(eventPublisher, never()).publierReservationConfirmee(any());
    }

    @Test
    void effectuerPaiement_dejaPaye_Conflit() {
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation(StatutReservation.EN_ATTENTE_PAIEMENT)));
        when(paiementRepository.existsByIdReservationAndStatut(1L, StatutPaiement.VALIDE)).thenReturn(true);

        assertThatThrownBy(() -> service.effectuerPaiement(new PaiementRequest(1L, ModePaiement.ESPECES)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("déjà payée");
    }
}