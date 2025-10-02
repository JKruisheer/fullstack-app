package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.services.ClientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/clients")
public class ClientsController {
    private final ClientsService clientsService;
    private final ClientsControllerMapper clientsControllerMapper;

    public ClientsController(ClientsService clientsService, ClientsControllerMapper clientsControllerMapper) {
        this.clientsService = clientsService;
        this.clientsControllerMapper = clientsControllerMapper;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        //todo change this because this has to be a cookie
        List<ClientResponse> clientsResponse = clientsService.getAllClients().stream().map(clientsControllerMapper::toClientResponse).toList();
        return ResponseEntity.ok(clientsResponse);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientResponse> getClientById(@PathVariable("id") Long id) {
        ClientResponse clientResponse = clientsControllerMapper.toClientResponse(clientsService.getClientById(id));
        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable("id") Long id) {
        clientsService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    //TODO POST API to add a new user
    //TODO PATCH api to modify data.
}
