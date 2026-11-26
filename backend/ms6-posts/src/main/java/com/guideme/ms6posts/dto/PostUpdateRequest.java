package com.guideme.ms6posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @NotBlank @Size(max = 255) String titre,
        @NotBlank String contenu,
        @Size(max = 255) String imageUrl
) {
}
