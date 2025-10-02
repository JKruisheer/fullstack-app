package com.assignment.client_management.services;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.exceptions.UnknownClientException;
import com.assignment.client_management.repositories.ClientsRepository;
import com.assignment.client_management.services.mapper.ClientsServiceMapper;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import com.assignment.client_management.services.model.UpdateClientInformation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;
    private final ClientsServiceMapper clientsServiceMapper;

    public ClientsService(ClientsRepository clientsRepository, ClientsServiceMapper clientsServiceMapper) {
        this.clientsRepository = clientsRepository;
        this.clientsServiceMapper = clientsServiceMapper;
    }

    public List<ClientInformation> getAllClients() {
        return clientsRepository.findAll().stream().map(clientsServiceMapper::toClient
        ).toList();
    }

    public ClientInformation getClientById(Long id) {
        ClientEntity clientEntity = findOrThrowClientEntity(id);
        return clientsServiceMapper.toClient(clientEntity);
    }

    public void deleteClientById(Long id) {
        ClientEntity clientEntity = findOrThrowClientEntity(id);
        clientsRepository.delete(clientEntity);
    }

    public Long createClient(NewClient newClient) {
        ClientEntity clientEntity = clientsServiceMapper.toClientEntity(newClient);
        //todo add some validation here, because email is mandatory, we want to control that and not rely on the db unique constraint violations
        return clientsRepository.save(clientEntity).getId();
    }

    public void updateClient(Long id, UpdateClientInformation updateClientInformation) {
        //todo add validation here as well, share with the createClients
        ClientEntity clientEntity = findOrThrowClientEntity(id);
        clientEntity.setDisplayName(updateClientInformation.displayName());
        clientEntity.setDetails(updateClientInformation.details());
        clientEntity.setActive(updateClientInformation.active());
        clientEntity.setLocation(updateClientInformation.location());
        clientsRepository.save(clientEntity);
    }

    private ClientEntity findOrThrowClientEntity(Long id) {
        return clientsRepository.findById(id).orElseThrow(() -> new UnknownClientException(String.format("Client with id %d not found", id)));
    }
}
