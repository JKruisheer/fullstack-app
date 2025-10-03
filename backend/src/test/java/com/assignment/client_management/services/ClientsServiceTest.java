package com.assignment.client_management.services;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.exceptions.DataValidationException;
import com.assignment.client_management.exceptions.UnknownClientException;
import com.assignment.client_management.repositories.ClientsRepository;
import com.assignment.client_management.services.mapper.ClientsServiceMapper;
import com.assignment.client_management.services.model.ClientInformation;
import com.assignment.client_management.services.model.NewClient;
import com.assignment.client_management.services.model.UpdateClientInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientsServiceTest {
    private static final Long ID = 1L;

    @Mock
    private ClientsRepository clientsRepository;

    @Mock
    private ClientsServiceMapper clientsServiceMapper;

    @InjectMocks
    private ClientsService clientsService;

    @Test
    void testGetClientsShouldReturnMappedClients() {
        ClientEntity mockedEntity = mock(ClientEntity.class);
        when(clientsRepository.findAll()).thenReturn(List.of(mockedEntity));

        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsServiceMapper.toClientInformation(mockedEntity)).thenReturn(mockedClientInformation);

        List<ClientInformation> actual = clientsService.getClients();

        assertEquals(1, actual.size());
        assertEquals(mockedClientInformation, actual.get(0));

        verify(clientsRepository, times(1)).findAll();
        verify(clientsServiceMapper, times(1)).toClientInformation(mockedEntity);
    }

    @Test
    void testGetClientByIdShouldThrowIfClientNotFound() {
        when(clientsRepository.findById(ID)).thenReturn(Optional.empty());

        var exception = assertThrows(UnknownClientException.class, () -> clientsService.getClientById(ID));

        assertEquals("Client with id 1 not found", exception.getMessage());

        verify(clientsRepository, times(1)).findById(ID);
    }

    @Test
    void testGetClientByIdShouldReturnMappedClientIfFound() {
        ClientEntity mockedEntity = mock(ClientEntity.class);
        when(clientsRepository.findById(ID)).thenReturn(Optional.of(mockedEntity));

        ClientInformation mockedClientInformation = mock(ClientInformation.class);
        when(clientsServiceMapper.toClientInformation(mockedEntity)).thenReturn(mockedClientInformation);

        ClientInformation actual = clientsService.getClientById(ID);

        assertEquals(mockedClientInformation, actual);

        verify(clientsRepository, times(1)).findById(ID);
        verify(clientsServiceMapper, times(1)).toClientInformation(mockedEntity);
    }

    @Test
    void testDeleteClientByIdShouldThrowIfClientNotFound() {
        when(clientsRepository.findById(ID)).thenReturn(Optional.empty());

        var exception = assertThrows(UnknownClientException.class, () -> clientsService.deleteClientById(ID));

        assertEquals("Client with id 1 not found", exception.getMessage());

        verify(clientsRepository, times(1)).findById(ID);
    }

    @Test
    void testDeleteClientByIdShouldCallRepository() {
        ClientEntity mockedEntity = mock(ClientEntity.class);
        when(clientsRepository.findById(ID)).thenReturn(Optional.of(mockedEntity));

        clientsService.deleteClientById(ID);

        verify(clientsRepository, times(1)).findById(ID);
        verify(clientsRepository, times(1)).delete(mockedEntity);
    }

    @Test
    void testCreateClientShouldReturnCorrectId() {
        NewClient newClientMock = mock(NewClient.class);
        ClientEntity clientEntityMock = mock(ClientEntity.class);
        when(clientEntityMock.getId()).thenReturn(ID);
        when(clientsServiceMapper.toClientEntity(newClientMock)).thenReturn(clientEntityMock);
        when(clientsRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

        when(clientsRepository.save(clientEntityMock)).thenReturn(clientEntityMock);

        Long actual = clientsService.createClient(newClientMock);

        assertEquals(ID, actual);

        verify(clientsRepository, times(1)).save(clientEntityMock);
        verify(clientsServiceMapper, times(1)).toClientEntity(newClientMock);
    }

    @Test
    void testCreateClientShouldThrowIfEmailAlreadyExists() {
        NewClient newClientMock = mock(NewClient.class);
        ClientEntity clientEntityMock = mock(ClientEntity.class);
        when(clientsServiceMapper.toClientEntity(newClientMock)).thenReturn(clientEntityMock);

        when(clientsRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(clientEntityMock));

        var exception = assertThrows(DataValidationException.class, () -> clientsService.createClient(newClientMock));

        assertEquals("This email is already used", exception.getMessage());
    }

    @Test
    void testUpdateClientShouldThrowIfClientNotFound() {
        UpdateClientInformation mockedUpdateClientInformation = mock(UpdateClientInformation.class);
        when(mockedUpdateClientInformation.details()).thenReturn("Details");
        when(clientsRepository.findById(ID)).thenReturn(Optional.empty());

        var exception = assertThrows(UnknownClientException.class, () -> clientsService.updateClient(ID, mockedUpdateClientInformation));

        assertEquals("Client with id 1 not found", exception.getMessage());

        verify(clientsRepository, times(1)).findById(ID);
    }

    @Test
    void testUpdateClientShouldUpdateTheEntity() {
        ClientEntity clientEntityMock = mock(ClientEntity.class);
        when(clientsRepository.findById(ID)).thenReturn(Optional.of(clientEntityMock));

        UpdateClientInformation updateClientInformation = new UpdateClientInformation("Display name", "details", false, "Utrecht");

        clientsService.updateClient(ID, updateClientInformation);

        verify(clientEntityMock).setDisplayName(updateClientInformation.displayName());
        verify(clientEntityMock).setDetails(updateClientInformation.details());
        verify(clientEntityMock).setActive(updateClientInformation.active());
        verify(clientEntityMock).setLocation(updateClientInformation.location());

        verify(clientsRepository, times(1)).findById(ID);
        verify(clientsRepository, times(1)).save(clientEntityMock);
    }

    @Test
    void updateClientShouldThrowIfDetailsIsTooLong() {
        UpdateClientInformation updateClientInformation = new UpdateClientInformation("Display name", "a".repeat(1025), false, "Utrecht");

        var exception = assertThrows(DataValidationException.class, () -> clientsService.updateClient(ID, updateClientInformation));

        assertEquals("This provided details are not valid.", exception.getMessage());
    }
}
