package com.guideme.ms1users.dto;

import jakarta.validation.constraints.Size;

public record ProfilTouristeRequest(
        @Size(max = 255) String preferences,
        @Size(max = 100) String pays,
        @Size(max = 100) String langue
) {}