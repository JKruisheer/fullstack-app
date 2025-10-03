package com.assignment.client_management.integration;

import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.controllers.model.PatchClientRequest;
import com.assignment.client_management.controllers.problems.UnknownClientProblem;
import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.repositories.ClientsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ClientsIntegrationTest {
    private static final String FULL_NAME = "John Doe";
    private static final String DISPLAY_NAME = "JD";
    private static final String EMAIL = "john@doe.com";
    private static final String DETAILS = "Some details";
    private static final boolean ACTIVE = true;
    private static final String LOCATION = "New York";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientsRepository clientsRepository;

    @BeforeEach
    void prepare() {
        this.clientsRepository.deleteAll();
    }

    @Test
    void getClientsShouldReturnEmptyListWithNoRecords() {
        ResponseEntity<ClientResponse[]> response = restTemplate.getForEntity("/clients", ClientResponse[].class);
        ClientResponse[] clients = response.getBody();

        assertEquals(0, clients.length);
    }

    @Test
    void getClientsShouldReturnFilledList() {
        insertClientRecord();

        ResponseEntity<ClientResponse[]> response = restTemplate.getForEntity("/clients", ClientResponse[].class);
        ClientResponse[] clients = response.getBody();

        assertEquals(1, clients.length);
        ClientResponse actual = clients[0];
        assertEquals(FULL_NAME, actual.fullName());
        assertEquals(DISPLAY_NAME, actual.displayName());
        assertEquals(EMAIL, actual.email());
        assertEquals(DETAILS, actual.details());
        assertEquals(ACTIVE, actual.active());
        assertEquals(LOCATION, actual.location());
    }

    @Test
    void getClientByIdReturnsNotFound() {
        Long unknownId = 99L;
        ResponseEntity<UnknownClientProblem> response = restTemplate.getForEntity("/clients/" + unknownId, UnknownClientProblem.class);

        assertEquals(NOT_FOUND, response.getStatusCode());
        UnknownClientProblem unknownClientProblem = response.getBody();

        assertEquals(String.format("Client with id %d not found", unknownId), unknownClientProblem.getTranslation());
        assertEquals(404, unknownClientProblem.getStatus());
    }

    @Test
    void getClientByIdReturnsClientResponse() {
        ClientEntity entity = insertClientRecord();

        ResponseEntity<ClientResponse> response = restTemplate.getForEntity("/clients/" + entity.getId(), ClientResponse.class);
        ClientResponse actual = response.getBody();

        assertEquals(FULL_NAME, actual.fullName());
        assertEquals(DISPLAY_NAME, actual.displayName());
        assertEquals(EMAIL, actual.email());
        assertEquals(DETAILS, actual.details());
        assertEquals(ACTIVE, actual.active());
        assertEquals(LOCATION, actual.location());
    }

    @Test
    void deleteClientByIdReturnsNotFound() {
        Long unknownId = 99L;
        ResponseEntity<UnknownClientProblem> response = restTemplate.exchange(
                "/clients/" + unknownId,
                HttpMethod.DELETE,
                null,
                UnknownClientProblem.class
        );

        assertEquals(NOT_FOUND, response.getStatusCode());
        UnknownClientProblem unknownClientProblem = response.getBody();

        assertEquals(String.format("Client with id %d not found", unknownId), unknownClientProblem.getTranslation());
        assertEquals(404, unknownClientProblem.getStatus());
    }

    @Test
    void deleteClientByIdRemovesClient() {
        ClientEntity entity = insertClientRecord();

        restTemplate.delete(URI.create("/clients/" + entity.getId()));

        Optional<ClientEntity> clientEntity = clientsRepository.findById(entity.getId());

        assertTrue(clientEntity.isEmpty());
    }

    @Test
    void createClientShouldReturnCorrectLocation() {
        NewClientRequest newClientRequest = new NewClientRequest(
                FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION
        );
        HttpEntity<NewClientRequest> requestEntity = new HttpEntity<>(newClientRequest);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/clients",
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        assertEquals(CREATED, response.getStatusCode());
        URI uri = response.getHeaders().getLocation();
        assertNotNull(uri);

        ResponseEntity<ClientResponse> createdClient = restTemplate.getForEntity(uri.getPath(), ClientResponse.class);
        assertNotNull(createdClient.getBody());
    }

    @Test
    void updateClientShouldUpdateTheFields() {
        ClientEntity entity = insertClientRecord();
        PatchClientRequest patchClientRequest = new PatchClientRequest("New display name", "new details", false, "Secret location");
        HttpEntity<PatchClientRequest> requestEntity = new HttpEntity<>(patchClientRequest);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/clients/" + entity.getId(),
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );

        assertEquals(NO_CONTENT, response.getStatusCode());

        ClientEntity updatedEntity = clientsRepository.findById(entity.getId()).get();

        assertEquals(patchClientRequest.displayName(), updatedEntity.getDisplayName());
        assertEquals(patchClientRequest.details(), updatedEntity.getDetails());
        assertEquals(patchClientRequest.active(), updatedEntity.isActive());
        assertEquals(patchClientRequest.location(), updatedEntity.getLocation());
    }

    private ClientEntity insertClientRecord() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFullName(FULL_NAME);
        clientEntity.setDisplayName(DISPLAY_NAME);
        clientEntity.setEmail(EMAIL);
        clientEntity.setDetails(DETAILS);
        clientEntity.setActive(ACTIVE);
        clientEntity.setLocation(LOCATION);
        return clientsRepository.save(clientEntity);
    }
}
