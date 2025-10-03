package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.controllers.model.PatchClientRequest;
import com.assignment.client_management.controllers.problems.UnknownClientProblem;
import com.assignment.client_management.services.ClientsService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<ClientResponse>> getClients() {
        //todo change this because this has to be a cookie
        List<ClientResponse> clientsResponse = clientsService.getClients().stream().map(clientsControllerMapper::toClientResponse).toList();
        return ResponseEntity.ok(clientsResponse);
    }

    @ApiResponse(
            responseCode = "404",
            description = "Client not found.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnknownClientProblem.class)
            )
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientResponse> getClientById(@PathVariable("id") final Long id) {
        ClientResponse clientResponse = clientsControllerMapper.toClientResponse(clientsService.getClientById(id));
        return ResponseEntity.ok(clientResponse);
    }

    @ApiResponse(
            responseCode = "404",
            description = "Client not found.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UnknownClientProblem.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable("id") final Long id) {
        clientsService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody final NewClientRequest newClientRequest) {
        Long id = clientsService.createClient(clientsControllerMapper.toNewClient(newClientRequest));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateClient(@PathVariable final Long id, @RequestBody final PatchClientRequest patchClientRequest) {
        clientsService.updateClient(id, clientsControllerMapper.toUpdateClientInformation(patchClientRequest));
        return ResponseEntity.noContent().build();
    }
}
