package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.Disponibilite;
import com.guideme.ms1users.entity.TypeProfessionnel;

public record ProfilProfessionnelResponse(
        Long idProfil, Long idUtilisateur, TypeProfessionnel typeProfessionnel,
        String description, Disponibilite disponibilite, String langues, Integer experienceAns
) {}