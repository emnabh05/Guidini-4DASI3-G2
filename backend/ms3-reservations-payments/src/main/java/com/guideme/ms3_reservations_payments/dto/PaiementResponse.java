package com.guideme.ms3_reservations_payments.dto;

import com.guideme.ms3_reservations_payments.entity.Paiement;
import com.guideme.ms3_reservations_payments.enums.ModePaiement;
import com.guideme.ms3_reservations_payments.enums.StatutPaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementResponse(
        Long idPaiement,
        Long idReservation,
        BigDecimal montant,
        ModePaiement mode,
        LocalDateTime datePaiement,
        StatutPaiement statut
) {
    public static PaiementResponse from(Paiement p) {
        return new PaiementResponse(
                p.getIdPaiement(), p.getIdReservation(), p.getMontant(),
                p.getMode(), p.getDatePaiement(), p.getStatut());
    }
}
