package com.assignment.client_management.services;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.exceptions.UnknownClientException;
import com.assignment.client_management.repositories.ClientsRepository;
import com.assignment.client_management.services.mapper.ClientsServiceMapper;
import com.assignment.client_management.services.model.Client;
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

    public List<Client> getAllClients() {
        return clientsRepository.findAll().stream().map(clientsServiceMapper::toClient
        ).toList();
    }

    public Client getClientById(Long id) {
        ClientEntity clientEntity = clientsRepository.findById(id).orElseThrow(() -> new UnknownClientException(String.format("Client with id %d not found", id)));
        return clientsServiceMapper.toClient(clientEntity);
    }
}
