package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.controllers.model.PatchClientRequest;
import com.assignment.client_management.controllers.problems.Problem;
import com.assignment.client_management.controllers.problems.UnknownClientProblem;
import com.assignment.client_management.exceptions.DataInputException;
import com.assignment.client_management.services.ClientsService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
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
    private final ClientCookieHandler clientCookieHandler;

    public ClientsController(final ClientsService clientsService, final ClientsControllerMapper clientsControllerMapper, final ClientCookieHandler clientCookieHandler) {
        this.clientsService = clientsService;
        this.clientsControllerMapper = clientsControllerMapper;
        this.clientCookieHandler = clientCookieHandler;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientResponse>> getClients(HttpServletResponse response) {
        final List<ClientResponse> clientsResponse = clientsService.getClients().stream().map(clientsControllerMapper::toClientResponse).toList();
        response.setHeader(HttpHeaders.SET_COOKIE, clientCookieHandler.buildResponseCookieString(clientsResponse.size()));
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
        final ClientResponse clientResponse = clientsControllerMapper.toClientResponse(clientsService.getClientById(id));
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

    @ApiResponse(
            responseCode = "400",
            description = "Data input problem.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)
            )
    )
    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody final NewClientRequest newClientRequest) {
        validateNewClientRequest(newClientRequest);
        final Long id = clientsService.createClient(clientsControllerMapper.toNewClient(newClientRequest));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    private void validateNewClientRequest(final NewClientRequest newClientRequest) {
        if (newClientRequest.fullName() == null || newClientRequest.displayName() == null || newClientRequest.email() == null) {
            throw new DataInputException("Invalid new client request");
        }
    }

    @ApiResponse(
            responseCode = "400",
            description = "Data input problem.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class)
            )
    )
    @PatchMapping("{id}")
    public ResponseEntity<Void> updateClient(@PathVariable final Long id, @RequestBody final PatchClientRequest patchClientRequest) {
        validatePatchClientRequest(patchClientRequest);
        clientsService.updateClient(id, clientsControllerMapper.toUpdateClientInformation(patchClientRequest));
        return ResponseEntity.noContent().build();
    }

    private void validatePatchClientRequest(final PatchClientRequest patchClientRequest) {
        if (patchClientRequest.displayName() == null) {
            throw new DataInputException("Invalid patch client request");
        }
    }
}
