package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.Disponibilite;
import com.guideme.ms1users.entity.TypeProfessionnel;

public record ProfessionnelResponse(
        Long idUtilisateur, String nom, String prenom, TypeProfessionnel typeProfessionnel,
        String description, Disponibilite disponibilite, String langues, Integer experienceAns
) {}