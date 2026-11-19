package com.guideme.ms3_reservations_payments.controller;

import com.guideme.ms3_reservations_payments.dto.AnnulationResponse;
import com.guideme.ms3_reservations_payments.dto.ParticipantsRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationRequest;
import com.guideme.ms3_reservations_payments.dto.ReservationResponse;
import com.guideme.ms3_reservations_payments.enums.StatutReservation;
import com.guideme.ms3_reservations_payments.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> creer(@Valid @RequestBody ReservationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.creerReservation(req));
    }

    @GetMapping
    public List<ReservationResponse> lister(@RequestParam(required = false) StatutReservation statut) {
        return reservationService.getReservations(statut);
    }

    @GetMapping("/client/{idClient}")
    public List<ReservationResponse> parClient(@PathVariable Long idClient) {
        return reservationService.getReservationsClient(idClient);
    }

    @GetMapping("/{id}")
    public ReservationResponse parId(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

    @PutMapping("/{id}/participants")
    public ReservationResponse modifierParticipants(@PathVariable Long id,
                                                    @Valid @RequestBody ParticipantsRequest req) {
        return reservationService.modifierParticipants(id, req);
    }

    @PatchMapping("/{id}/annulation")
    public AnnulationResponse annuler(@PathVariable Long id) {
        return reservationService.annulerReservation(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        reservationService.supprimerReservation(id);
        return ResponseEntity.noContent().build();
    }
}