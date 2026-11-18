package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.Disponibilite;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProfilProfessionnelRequest(
        @Size(max = 2000) String description,
        Disponibilite disponibilite,
        @Size(max = 255) String langues,
        @Min(value = 0, message = "L'expérience doit être positive") Integer experienceAns
) {}