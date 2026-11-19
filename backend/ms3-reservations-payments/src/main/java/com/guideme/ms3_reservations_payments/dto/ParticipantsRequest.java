package com.guideme.ms3_reservations_payments.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ParticipantsRequest(
        @NotEmpty List<String> participants
) {
}
