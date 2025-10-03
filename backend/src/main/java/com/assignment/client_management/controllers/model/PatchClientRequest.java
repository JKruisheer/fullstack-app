package com.assignment.client_management.controllers.model;

import jakarta.validation.constraints.NotNull;

public record PatchClientRequest(
        @NotNull String displayName,
        @NotNull String details,
        @NotNull boolean active,
        @NotNull String location
) {
}
