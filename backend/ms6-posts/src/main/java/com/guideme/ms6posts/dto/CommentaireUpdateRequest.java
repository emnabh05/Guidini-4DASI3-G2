package com.guideme.ms6posts.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentaireUpdateRequest(
        @NotBlank String contenu
) {
}
