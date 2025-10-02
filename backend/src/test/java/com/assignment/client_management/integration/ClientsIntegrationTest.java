package com.assignment.client_management.integration;

import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.repositories.ClientsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ClientResponse expected = createExpectedClientResponse();

        assertEquals(expected, actual);
    }

    private ClientResponse createExpectedClientResponse() {
        return new ClientResponse(
                FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION
        );
    }

    private void insertClientRecord() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFullName(FULL_NAME);
        clientEntity.setDisplayName(DISPLAY_NAME);
        clientEntity.setEmail(EMAIL);
        clientEntity.setDetails(DETAILS);
        clientEntity.setActive(ACTIVE);
        clientEntity.setLocation(LOCATION);
        clientsRepository.save(clientEntity);
    }
}
