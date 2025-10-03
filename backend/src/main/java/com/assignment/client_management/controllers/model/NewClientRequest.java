package com.assignment.client_management.controllers.model;

import jakarta.validation.constraints.NotNull;

public record NewClientRequest(
        @NotNull String fullName,
        @NotNull String displayName,
        @NotNull String email,
        @NotNull String details,
        @NotNull boolean active,
        @NotNull String location
) {
}
