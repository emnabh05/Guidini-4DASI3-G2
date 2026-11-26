package com.guideme.ms6posts.dto;

import com.guideme.ms6posts.enums.StatutPost;
import jakarta.validation.constraints.NotNull;

public record StatutRequest(
        @NotNull StatutPost statut
) {
}
