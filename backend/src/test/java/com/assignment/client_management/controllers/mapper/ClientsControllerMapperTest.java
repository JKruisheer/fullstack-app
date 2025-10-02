package com.assignment.client_management.controllers.mapper;

import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientsControllerMapperTest {
    private static final Long ID = 1L;
    private static final String FULL_NAME = "John Doe";
    private static final String DISPLAY_NAME = "JD";
    private static final String EMAIL = "john@doe.com";
    private static final String DETAILS = "Some details";
    private static final boolean ACTIVE = true;
    private static final String LOCATION = "New York";

    private final ClientsControllerMapper clientsControllerMapper = new ClientsControllerMapper();

    @Test
    void testToClientResponse() {
        ClientResponse actual = clientsControllerMapper.toClientResponse(createClientInformation());
        ClientResponse expected = createExpectedClientResponse();
        assertEquals(expected, actual);
    }

    private ClientInformation createClientInformation() {
        return new ClientInformation(ID, FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    private ClientResponse createExpectedClientResponse() {
        return new ClientResponse(ID, FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    @Test
    void testToNewClient() {
        NewClient actual = clientsControllerMapper.toNewClient(createNewClientRequest());
        NewClient expected = createdExpectedNewClient();
        assertEquals(expected, actual);
    }

    private NewClient createdExpectedNewClient() {
        return new NewClient(FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    private NewClientRequest createNewClientRequest() {
        return new NewClientRequest(FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }
}
