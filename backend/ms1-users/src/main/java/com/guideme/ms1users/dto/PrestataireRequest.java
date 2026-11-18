package com.guideme.ms1users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PrestataireRequest(
        @NotBlank(message = "La raison sociale est obligatoire") @Size(max = 255) String raisonSociale,
        @Size(max = 50) String matriculeFiscal,
        @Size(max = 255) String adresse,
        @Size(max = 2000) String description
) {}