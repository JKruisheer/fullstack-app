package com.assignment.client_management.services;

import com.assignment.client_management.entities.ClientEntity;
import com.assignment.client_management.repositories.ClientsRepository;
import com.assignment.client_management.services.mapper.ClientsServiceMapper;
import com.assignment.client_management.services.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientsServiceTest {

    @Mock
    private ClientsRepository clientsRepository;

    @Mock
    private ClientsServiceMapper clientsServiceMapper;

    @InjectMocks
    private ClientsService clientsService;

    @Test
    void testGetAllClientsShouldReturnMappedClients() {
        ClientEntity mockedEntity = mock(ClientEntity.class);
        when(clientsRepository.findAll()).thenReturn(List.of(mockedEntity));

        Client mockedClient = mock(Client.class);
        when(clientsServiceMapper.toClient(mockedEntity)).thenReturn(mockedClient);

        List<Client> actual = clientsService.getAllClients();

        assertEquals(1, actual.size());
        assertEquals(mockedClient, actual.get(0));

        verify(clientsRepository, times(1)).findAll();
        verify(clientsServiceMapper, times(1)).toClient(mockedEntity);
    }
}
