package com.assignment.client_management.services.mapper;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.services.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientsServiceMapperTest {

    private static final Long ID = 1L;
    private static final String FULL_NAME = "John Doe";
    private static final String DISPLAY_NAME = "JD";
    private static final String EMAIL = "john@doe.com";
    private static final String DETAILS = "Some details";
    private static final boolean ACTIVE = true;
    private static final String LOCATION = "New York";

    private final ClientsServiceMapper clientsServiceMapper = new ClientsServiceMapper();

    @Test
    void testClientEntityMapper() {
        Client actual = clientsServiceMapper.toClient(createTestClientEntity());
        Client expected = createExpectedClient();
        assertEquals(expected, actual);
    }

    private Client createExpectedClient() {
        return new Client(ID, FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    private ClientEntity createTestClientEntity() {
        ClientEntity entity = new ClientEntity();
        entity.setId(ID);
        entity.setFullName(FULL_NAME);
        entity.setDisplayName(DISPLAY_NAME);
        entity.setEmail(EMAIL);
        entity.setDetails(DETAILS);
        entity.setActive(ACTIVE);
        entity.setLocation(LOCATION);
        return entity;
    }

}
