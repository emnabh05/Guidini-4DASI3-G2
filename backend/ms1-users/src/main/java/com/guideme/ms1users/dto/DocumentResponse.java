package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.StatutVerification;
import com.guideme.ms1users.entity.TypeDocument;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentResponse(
        Long idDocument, Long idUtilisateur, TypeDocument typeDocument, String numeroDocument,
        LocalDate dateExpiration, String cheminFichier, StatutVerification statutVerification,
        String motifRefus, LocalDateTime dateDepot, LocalDateTime dateVerification
) {}