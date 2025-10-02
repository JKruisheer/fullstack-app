package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.services.ClientsService;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class ClientsControllerTest {
    private static final Long ID = 1L;

    @Mock
    private ClientsService clientsService;

    @Mock
    private ClientsControllerMapper clientsControllerMapper;

    @InjectMocks
    private ClientsController clientsController;

    @Test
    void getAllClientsShouldReturnMappedClientResponse() {
        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsService.getAllClients()).thenReturn(List.of(mockedClientInformation));

        ClientResponse mockedClientResponse = mock(ClientResponse.class);
        when(clientsControllerMapper.toClientResponse(mockedClientInformation)).thenReturn(mockedClientResponse);

        ResponseEntity<List<ClientResponse>> actual = clientsController.getAllClients();

        assertEquals(OK, actual.getStatusCode());

        List<ClientResponse> response = actual.getBody();
        assertEquals(1, response.size());
        assertEquals(mockedClientResponse, response.get(0));

        verify(clientsService, times(1)).getAllClients();
        verify(clientsControllerMapper, times(1)).toClientResponse(mockedClientInformation);
    }

    @Test
    void getClientByIdShouldReturnMappedClientResponse() {
        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsService.getClientById(ID)).thenReturn(mockedClientInformation);

        ClientResponse mockedClientResponse = mock(ClientResponse.class);
        when(clientsControllerMapper.toClientResponse(mockedClientInformation)).thenReturn(mockedClientResponse);

        ResponseEntity<ClientResponse> actual = clientsController.getClientById(ID);

        assertEquals(OK, actual.getStatusCode());

        assertEquals(mockedClientResponse, actual.getBody());

        verify(clientsService, times(1)).getClientById(ID);
        verify(clientsControllerMapper, times(1)).toClientResponse(mockedClientInformation);
    }

    @Test
    void deleteClientByIdShouldReturnNoContentAndCallService() {
        ResponseEntity<Void> actual = clientsController.deleteClientById(ID);
        assertEquals(NO_CONTENT, actual.getStatusCode());

        verify(clientsService, times(1)).deleteClientById(ID);
    }

    @Test
    void createClientShouldReturnCreatedLocation() {
        mockHttpServlet();
        NewClientRequest request = mock(NewClientRequest.class);
        NewClient newClient = mock(NewClient.class);
        when(clientsControllerMapper.toNewClient(request)).thenReturn(newClient);
        when(clientsService.createClient(newClient)).thenReturn(ID);

        ResponseEntity<Void> response = clientsController.createClient(request);

        // Assert
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().endsWith("/" + ID));

        verify(clientsControllerMapper, times(1)).toNewClient(request);
        verify(clientsService, times(1)).createClient(newClient);
    }

    private void mockHttpServlet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
}
