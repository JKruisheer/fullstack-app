package com.assignment.client_management.services.mapper;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import org.springframework.stereotype.Component;

@Component
public class ClientsServiceMapper {
    public ClientInformation toClientInformation(final ClientEntity clientEntity) {
        return new ClientInformation(
                clientEntity.getId(),
                clientEntity.getFullName(),
                clientEntity.getDisplayName(),
                clientEntity.getEmail(),
                clientEntity.getDetails(),
                clientEntity.isActive(),
                clientEntity.getLocation()
        );
    }

    public ClientEntity toClientEntity(final NewClient newClient) {
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
