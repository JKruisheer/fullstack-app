import {createServiceFactory, SpectatorService} from '@ngneat/spectator';
import {ClientFacade} from './client.facade';
import {ClientResponse, ClientsControllerService} from '../../../../api';
import {Observable, of, throwError} from 'rxjs';
import {HttpEvent} from '@angular/common/http';

describe('ClientFacade', () => {
  let spectator: SpectatorService<ClientFacade>;
  let clientControllerService: jasmine.SpyObj<ClientsControllerService>;

  const testClient: ClientResponse = {
    id: 1,
    fullName: 'John Doe',
    displayName: 'jdoe',
    email: 'john.doe@example.com',
    active: true,
    details: 'Test client',
    location: 'Test City'
  };

  const createService = createServiceFactory({
    service: ClientFacade,
    mocks: [ClientsControllerService]
  });

  beforeEach(() => {
    spectator = createService();
    clientControllerService = spectator.inject(ClientsControllerService);
  });

  it('should create', () => {
    expect(spectator.service).toBeTruthy();
  });

  it('should load clients and update state on success', () => {
    clientControllerService.getClients.and.returnValue(
      of([testClient]) as unknown as Observable<HttpEvent<ClientResponse[]>>
    );

    spectator.service.loadClients();

    expect(spectator.service.isLoading()).toBeFalse();
    expect(spectator.service.originalClientList()).toEqual([testClient]);
    expect(spectator.service.errorMessage()).toBe('');
  });

  it('should set errorMessage and isLoading on error', () => {
    clientControllerService.getClients.and.returnValue(throwError(() => new Error('fail')));

    spectator.service.loadClients();

    expect(spectator.service.isLoading()).toBeFalse();
    expect(spectator.service.errorMessage()).toBe('Something went wrong with loading the clients.');
    expect(spectator.service.originalClientList()).toEqual([]);
  });
});
