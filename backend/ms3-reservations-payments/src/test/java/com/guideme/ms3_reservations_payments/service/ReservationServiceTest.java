package com.guideme.ms3_reservations_payments.service;

import com.guideme.ms3_reservations_payments.dto.AnnulationResponse;
import com.guideme.ms3_reservations_payments.dto.ParticipantsRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationResponse;
import com.guideme.ms3_reservations_payments.entity.Reservation;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.enums.TypeReservation;
import com.guideme.ms3_reservations_payments.exception.BadRequestException;
import com.guideme.ms3_reservations_payments.exception.ConflictException;
import com.guideme.ms3_reservations_payments.repository.PaiementRepository;
import com.guideme.ms3_reservations_payments.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaiementRepository paiementRepository;

    @InjectMocks
    private ReservationService service;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(service, "prixUnitaireDefaut", new BigDecimal("50"));
        ReflectionTestUtils.setField(service, "expirationMinutes", 15L);
    }

    private Reservation reservation(StatutReservation statut, LocalDateTime date) {
        return Reservation.builder()
                .idReservation(1L)
                .idClient(1L)
                .typeReservation(TypeReservation.ACTIVITE)
                .idActivite(10L)
                .nombrePlaces(3)
                .participants(new ArrayList<>(List.of("Emna", "Sami", "Yasmine")))
                .montantTotal(new BigDecimal("150"))
                .dateReservation(date)
                .statut(statut)
                .build();
    }

    @Test
    void calculerMontant_3PlacesA50Tnd_Donne150() {
        assertThat(service.calculerMontant(new BigDecimal("50"), 3)).isEqualByComparingTo("150");
    }

    @Test
    void creerReservation_valide_montant150EtEnAttentePaiement() {
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));
        ReservationRequest req = new ReservationRequest(1L, TypeReservation.ACTIVITE, 10L, null, 3,
                List.of("Emna", "Sami", "Yasmine"));

        ReservationResponse rep = service.creerReservation(req);

        assertThat(rep.montantTotal()).isEqualByComparingTo("150");
        assertThat(rep.statut()).isEqualTo(StatutReservation.EN_ATTENTE_PAIEMENT);
    }

    @Test
    void creerReservation_3PlacesMais2Noms_Refusee() {
        ReservationRequest req = new ReservationRequest(1L, TypeReservation.ACTIVITE, 10L, null, 3,
                List.of("Emna", "Sami"));

        assertThatThrownBy(() -> service.creerReservation(req))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("participants");
    }

    @Test
    void creerReservation_activiteSansIdActivite_Refusee() {
        ReservationRequest req = new ReservationRequest(1L, TypeReservation.ACTIVITE, null, null, 1,
                List.of("Emna"));

        assertThatThrownBy(() -> service.creerReservation(req))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("idActivite");
    }

    @Test
    void annuler_payeeDansLes24h_Rembourse100Pour100() {
        Reservation r = reservation(StatutReservation.CONFIRMEE, LocalDateTime.now().minusHours(10));
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        AnnulationResponse rep = service.annulerReservation(1L);

        assertThat(rep.pourcentageRembourse()).isEqualTo(100);
        assertThat(rep.montantRembourse()).isEqualByComparingTo("150");
        assertThat(rep.statut()).isEqualTo(StatutReservation.ANNULEE);
    }

    @Test
    void annuler_payeeApres24h_Rembourse50Pour100() {
        Reservation r = reservation(StatutReservation.CONFIRMEE, LocalDateTime.now().minusHours(30));
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        AnnulationResponse rep = service.annulerReservation(1L);

        assertThat(rep.pourcentageRembourse()).isEqualTo(50);
        assertThat(rep.montantRembourse()).isEqualByComparingTo("75");
    }

    @Test
    void annuler_nonPayee_RembourseZero() {
        Reservation r = reservation(StatutReservation.EN_ATTENTE_PAIEMENT, LocalDateTime.now());
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        AnnulationResponse rep = service.annulerReservation(1L);

        assertThat(rep.pourcentageRembourse()).isZero();
        assertThat(rep.montantRembourse()).isEqualByComparingTo("0");
    }

    @Test
    void annuler_dejaAnnulee_Conflit() {
        Reservation r = reservation(StatutReservation.ANNULEE, LocalDateTime.now());
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> service.annulerReservation(1L)).isInstanceOf(ConflictException.class);
    }

    @Test
    void modifierParticipants_reservationAnnulee_Conflit() {
        Reservation r = reservation(StatutReservation.ANNULEE, LocalDateTime.now());
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> service.modifierParticipants(1L, new ParticipantsRequest(List.of("A", "B", "C"))))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void expirer_reservationNonPayeeDepuisPlusDe15Min_PasseAExpiree() {
        Reservation r = reservation(StatutReservation.EN_ATTENTE_PAIEMENT, LocalDateTime.now().minusMinutes(20));
        when(reservationRepository.findByStatutAndDateReservationBefore(
                eq(StatutReservation.EN_ATTENTE_PAIEMENT), any(LocalDateTime.class))).thenReturn(List.of(r));

        int nombre = service.expirerReservationsNonPayees();

        assertThat(nombre).isEqualTo(1);
        assertThat(r.getStatut()).isEqualTo(StatutReservation.EXPIREE);
    }
}