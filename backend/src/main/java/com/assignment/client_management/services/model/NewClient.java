package com.assignment.client_management.services.model;

public record NewClient(String fullName,
                        String displayName,
                        String email,
                        String details,
                        boolean active,
                        String location) {
}
