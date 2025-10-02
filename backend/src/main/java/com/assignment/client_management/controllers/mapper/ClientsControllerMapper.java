package com.assignment.client_management.controllers.mapper;

import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.services.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientsControllerMapper {
    public ClientResponse toClientResponse(Client client) {
        return new ClientResponse(
                client.id(),
                client.fullName(),
                client.displayName(),
                client.email(),
                client.details(),
                client.active(),
                client.location()
        );
    }
}
