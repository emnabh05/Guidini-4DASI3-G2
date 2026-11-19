package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.Role;
import com.guideme.ms1users.entity.StatutUtilisateur;
import java.time.LocalDateTime;

public record UtilisateurResponse(
        Long idUtilisateur, String nom, String prenom, String email, String telephone,
        Role role, StatutUtilisateur statut,
        LocalDateTime dateCreation, LocalDateTime dateMaj
) {}