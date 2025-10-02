package com.assignment.client_management.controllers.model;

public record ClientResponse(Long id, String fullName, String displayName, String email, String details, boolean active,
                             String location) {
}
