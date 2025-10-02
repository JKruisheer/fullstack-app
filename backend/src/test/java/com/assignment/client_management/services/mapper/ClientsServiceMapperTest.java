package com.assignment.client_management.services.mapper;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
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
    void testToClient() {
        ClientInformation actual = clientsServiceMapper.toClient(createClientEntity());
        ClientInformation expected = createExpectedClient();
        assertEquals(expected, actual);
    }

    private ClientInformation createExpectedClient() {
        return new ClientInformation(ID, FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    @Test
    void testToClientEntity() {
        ClientEntity actual = clientsServiceMapper.toClientEntity(createNewClient());
        assertEquals(FULL_NAME, actual.getFullName());
        assertEquals(DISPLAY_NAME, actual.getDisplayName());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(DETAILS, actual.getDetails());
        assertEquals(LOCATION, actual.getLocation());
        assertEquals(ACTIVE, actual.isActive());
    }

    private NewClient createNewClient() {
        return new NewClient(FULL_NAME, DISPLAY_NAME, EMAIL, DETAILS, ACTIVE, LOCATION);
    }

    private ClientEntity createClientEntity() {
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
