package com.assignment.client_management.controllers.mapper;

import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import org.springframework.stereotype.Component;

@Component
public class ClientsControllerMapper {
    public ClientResponse toClientResponse(ClientInformation clientInformation) {
        return new ClientResponse(
                clientInformation.id(),
                clientInformation.fullName(),
                clientInformation.displayName(),
                clientInformation.email(),
                clientInformation.details(),
                clientInformation.active(),
                clientInformation.location()
        );
    }

    public NewClient toNewClient(NewClientRequest newClientRequest) {
        return new NewClient(
                newClientRequest.fullName(),
                newClientRequest.displayName(),
                newClientRequest.email(),
                newClientRequest.details(),
                newClientRequest.active(),
                newClientRequest.location()
        );
    }
}
