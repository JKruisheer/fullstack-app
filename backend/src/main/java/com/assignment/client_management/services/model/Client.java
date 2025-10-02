package com.assignment.client_management.services.model;

public record Client(Long id, String fullName, String displayName, String email, String details, boolean active,
                     String location) {
}
