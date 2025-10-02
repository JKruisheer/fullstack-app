package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.services.ClientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        var clientsResponse = clientsService.getAllClients().stream().map(clientsControllerMapper::toClientResponse).toList();
        return ResponseEntity.ok(clientsResponse);
    }

    //TODO api to fetch a specific client by id
    //TODO PATCH api to modify data.
    //TODO delete api for a certain client

    //TODO CREATE A PROBLEM IF IT DOES NOT MATCH

}
