package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.TypeDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DocumentRequest(
        @NotNull(message = "idUtilisateur est obligatoire") Long idUtilisateur,
        @NotNull(message = "Le type de document est obligatoire") TypeDocument typeDocument,
        @Size(max = 100) String numeroDocument,
        LocalDate dateExpiration,
        @NotBlank(message = "Le chemin du fichier est obligatoire") @Size(max = 255) String cheminFichier
) {}