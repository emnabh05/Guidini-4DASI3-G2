package com.guideme.ms1users.dto;

public record PrestataireResponse(
        Long idAgence, Long idUtilisateur, String raisonSociale,
        String matriculeFiscal, String adresse, String description
) {}