package com.assignment.client_management.controllers;

import com.assignment.client_management.controllers.mapper.ClientsControllerMapper;
import com.assignment.client_management.controllers.model.ClientResponse;
import com.assignment.client_management.controllers.model.NewClientRequest;
import com.assignment.client_management.controllers.model.PatchClientRequest;
import com.assignment.client_management.exceptions.DataInputException;
import com.assignment.client_management.services.ClientsExportService;
import com.assignment.client_management.services.ClientsService;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import com.assignment.client_management.services.model.UpdateClientInformation;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private ClientCookieHandler clientCookieHandler;

    @Mock
    private ClientsExportService clientsExportService;

    @InjectMocks
    private ClientsController clientsController;

    @Test
    void getClientsShouldReturnMappedClientResponseAndSetCookie() {
        final int expectedClientCount = 1;
        final ClientInformation mockedClientInformation = mockClientInformation();
        final ClientResponse mockedClientResponse = mockClientResponse(mockedClientInformation);

        final String exceptedCookie = "expectedCookie";
        HttpServletResponse mockedHttpServetResponse = mock(HttpServletResponse.class);

        when(clientCookieHandler.buildResponseCookieString(expectedClientCount)).thenReturn(exceptedCookie);
        ResponseEntity<List<ClientResponse>> actual = clientsController.getClients(mockedHttpServetResponse);

        assertEquals(OK, actual.getStatusCode());

        List<ClientResponse> response = actual.getBody();
        assertEquals(expectedClientCount, response.size());
        assertEquals(mockedClientResponse, response.get(0));

        verify(mockedHttpServetResponse).setHeader(HttpHeaders.SET_COOKIE, exceptedCookie);
        verify(clientsService, times(1)).getClients();
        verify(clientsControllerMapper, times(1)).toClientResponse(mockedClientInformation);
        verify(clientCookieHandler, times(1)).buildResponseCookieString(expectedClientCount);
    }

    @Test
    void exportClientShouldWriteClientsAsCsv() throws IOException {
        final String expectedFileName = "filename";
        final ClientInformation mockedClientInformation = mockClientInformation();
        final ClientResponse mockedClientResponse = mockClientResponse(mockedClientInformation);

        HttpServletResponse mockedHttpServetResponse = mock(HttpServletResponse.class);
        PrintWriter mockedPrintWriter = mock(PrintWriter.class);
        when(mockedHttpServetResponse.getWriter()).thenReturn(mockedPrintWriter);
        clientsController.exportClients(mockedHttpServetResponse, expectedFileName);

        verify(mockedHttpServetResponse).setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.csv", expectedFileName));

        ArgumentCaptor<List<ClientResponse>> captor = ArgumentCaptor.forClass(List.class);
        verify(clientsExportService).writeClientsAsCsv(captor.capture(), eq(mockedPrintWriter));

        List<ClientResponse> capturedClients = captor.getValue();
        assertEquals(1, capturedClients.size());
        assertEquals(mockedClientResponse, capturedClients.get(0));
    }

    @Test
    void getClientByIdShouldReturnMappedClientResponse() {
        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsService.getClientById(ID)).thenReturn(mockedClientInformation);

        ClientResponse mockedClientResponse = mockClientResponse(mockedClientInformation);

        ResponseEntity<ClientResponse> actual = clientsController.getClientById(ID);

        assertEquals(OK, actual.getStatusCode());

        assertEquals(mockedClientResponse, actual.getBody());

        verify(clientsService, times(1)).getClientById(ID);
        verify(clientsControllerMapper, times(1)).toClientResponse(mockedClientInformation);
    }

    private ClientInformation mockClientInformation() {
        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsService.getClients()).thenReturn(List.of(mockedClientInformation));
        return mockedClientInformation;
    }

    private ClientResponse mockClientResponse(ClientInformation mockedClientInformation) {
        ClientResponse mockedClientResponse = mock(ClientResponse.class);
        when(clientsControllerMapper.toClientResponse(mockedClientInformation)).thenReturn(mockedClientResponse);
        return mockedClientResponse;
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
        NewClientRequest request = createNewClientRequestMock();
        NewClient newClient = mock(NewClient.class);
        when(clientsControllerMapper.toNewClient(request)).thenReturn(newClient);
        when(clientsService.createClient(newClient)).thenReturn(ID);

        ResponseEntity<Void> response = clientsController.createClient(request);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().endsWith("/" + ID));

        verify(clientsControllerMapper, times(1)).toNewClient(request);
        verify(clientsService, times(1)).createClient(newClient);
    }

    private NewClientRequest createNewClientRequestMock() {
        NewClientRequest request = mock(NewClientRequest.class);
        when(request.displayName()).thenReturn("Display name");
        when(request.fullName()).thenReturn("fullname");
        when(request.email()).thenReturn("email");
        return request;
    }

    private void mockHttpServlet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void createClientShouldThrowForInvalidNewClientRequest() {
        NewClientRequest request = mock(NewClientRequest.class);
        var exception = assertThrows(DataInputException.class, () -> clientsController.createClient(request));

        assertEquals("Invalid new client request", exception.getMessage());
    }

    @Test
    void updateClientShouldCallService() {
        PatchClientRequest patchClientRequestMock = createPatchClientRequestMock();
        UpdateClientInformation updateClientInformationMock = mock(UpdateClientInformation.class);
        when(clientsControllerMapper.toUpdateClientInformation(patchClientRequestMock)).thenReturn(updateClientInformationMock);

        ResponseEntity<Void> response = clientsController.updateClient(ID, patchClientRequestMock);
        assertEquals(NO_CONTENT, response.getStatusCode());

        verify(clientsControllerMapper, times(1)).toUpdateClientInformation(patchClientRequestMock);
        verify(clientsService, times(1)).updateClient(ID, updateClientInformationMock);
    }

    private PatchClientRequest createPatchClientRequestMock() {
        PatchClientRequest patchClientRequest = mock(PatchClientRequest.class);
        when(patchClientRequest.displayName()).thenReturn("display name");
        return patchClientRequest;
    }

    @Test
    void updateClientShouldThrowForInvalidPatchClientRequest() {
        PatchClientRequest request = mock(PatchClientRequest.class);
        var exception = assertThrows(DataInputException.class, () -> clientsController.updateClient(ID, request));

        assertEquals("Invalid patch client request", exception.getMessage());
    }
}
