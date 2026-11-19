package com.guideme.ms1users.dto;

public record ProfilTouristeResponse(
        Long idProfil, Long idUtilisateur, String preferences, String pays, String langue
) {}