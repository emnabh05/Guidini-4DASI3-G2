package com.guideme.ms1users.dto;

import com.guideme.ms1users.entity.Role;
import jakarta.validation.constraints.*;

public record UtilisateurRequest(
        @NotBlank(message = "Le nom est obligatoire") @Size(max = 100) String nom,
        @NotBlank(message = "Le prénom est obligatoire") @Size(max = 100) String prenom,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email,
        @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "Téléphone invalide") String telephone,
        @NotNull(message = "Le rôle est obligatoire") Role role
) {}