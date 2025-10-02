package com.assignment.client_management.services.model;

public record UpdateClientInformation(String displayName,
                                      String details,
                                      boolean active,
                                      String location) {
}
