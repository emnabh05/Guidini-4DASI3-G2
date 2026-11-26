package com.guideme.ms6posts.dto;

import com.guideme.ms6posts.enums.StatutPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull Long idAuteur,
        @NotBlank @Size(max = 255) String titre,
        @NotBlank String contenu,
        @Size(max = 255) String imageUrl,
        StatutPost statut
) {
}
