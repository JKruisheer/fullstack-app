package com.assignment.client_management.services.mapper;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.services.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientsServiceMapper {
    public Client toClient(ClientEntity entity) {
        return new Client(
                entity.getId(),
                entity.getFullName(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getDetails(),
                entity.isActive(),
                entity.getLocation()
        );
    }
}
