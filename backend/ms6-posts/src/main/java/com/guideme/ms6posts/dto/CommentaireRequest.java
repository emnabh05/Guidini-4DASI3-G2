package com.guideme.ms6posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentaireRequest(
        @NotNull Long idAuteur,
        @NotBlank String contenu,
        Long idCommentaireParent
) {
}
