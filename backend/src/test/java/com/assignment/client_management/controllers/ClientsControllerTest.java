package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.services.ClientsService;
import com.assignment.client_management.services.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class ClientsControllerTest {

    @Mock
    private ClientsService clientsService;

    @Mock
    private ClientsControllerMapper clientsControllerMapper;

    @InjectMocks
    private ClientsController clientsController;

    @Test
    void getAllClientsShouldReturnMappedClientResponse() {
        Client mockedClient = mock(Client.class);
        when(clientsService.getAllClients()).thenReturn(List.of(mockedClient));

        ClientResponse mockedClientResponse = mock(ClientResponse.class);
        when(clientsControllerMapper.toClientResponse(mockedClient)).thenReturn(mockedClientResponse);

        ResponseEntity<List<ClientResponse>> actual = clientsController.getAllClients();

        assertEquals(OK, actual.getStatusCode());

        List<ClientResponse> response = actual.getBody();
        assertEquals(1, response.size());
        assertEquals(mockedClientResponse, response.get(0));

        verify(clientsService, times(1)).getAllClients();
        verify(clientsControllerMapper, times(1)).toClientResponse(mockedClient);
    }
}
