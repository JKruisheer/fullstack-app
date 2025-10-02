package com.assignment.client_management.controllers.model;

public record PatchClientRequest(
        String displayName,
        String details,
        boolean active,
        String location
) {
}
