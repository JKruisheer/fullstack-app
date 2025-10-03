package com.assignment.client_management.controllers.model;

import jakarta.validation.constraints.NotNull;

public record PatchClientRequest(
        @NotNull String displayName,
        String details,
        @NotNull boolean active,
        String location
) {
}
