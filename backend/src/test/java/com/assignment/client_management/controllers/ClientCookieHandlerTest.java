package com.assignment.client_management.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientCookieHandlerTest {

    @InjectMocks
    private ClientCookieHandler clientCookieHandler;

    @Test
    void testHandlerShouldBuildResponseCookieWithClientCount() {
        int clientCount = 5;
        final String cookieString = clientCookieHandler.buildResponseCookieString(clientCount);

        assertTrue(cookieString.contains("RABO_CLIENTS=5"));
        assertTrue(cookieString.contains("Path=/"));
        assertTrue(cookieString.contains("Max-Age=86400"));
    }

}
