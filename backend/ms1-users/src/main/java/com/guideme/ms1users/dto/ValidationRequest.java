package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.StatutVerification;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ValidationRequest(
        @NotNull(message = "La décision est obligatoire (VALIDE ou REFUSE)") StatutVerification decision,
        @Size(max = 500) String motifRefus
) {}
