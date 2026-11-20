package com.guideme.ms3_reservations_payments.controller;

import com.guideme.ms3_reservations_payments.dto.PaiementRequest;
import com.guideme.ms3_reservations_payments.dto.PaiementResponse;
import com.guideme.ms3_reservations_payments.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<PaiementResponse> payer(@Valid @RequestBody PaiementRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.effectuerPaiement(req));
    }

    @GetMapping("/{id}")
    public PaiementResponse parId(@PathVariable Long id) {
        return paiementService.getPaiement(id);
    }

    @GetMapping("/reservation/{idReservation}")
    public List<PaiementResponse> parReservation(@PathVariable Long idReservation) {
        return paiementService.getPaiementsReservation(idReservation);
    }
}