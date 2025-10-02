package com.assignment.client_management.services.mapper;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import org.springframework.stereotype.Component;

@Component
public class ClientsServiceMapper {
    public ClientInformation toClient(ClientEntity entity) {
        return new ClientInformation(
                entity.getId(),
                entity.getFullName(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getDetails(),
                entity.isActive(),
                entity.getLocation()
        );
    }

    public ClientEntity toClientEntity(NewClient newClient) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFullName(newClient.fullName());
        clientEntity.setDisplayName(newClient.displayName());
        clientEntity.setEmail(newClient.email());
        clientEntity.setDetails(newClient.details());
        clientEntity.setLocation(newClient.location());
        clientEntity.setActive(newClient.active());
        return clientEntity;
    }
}
