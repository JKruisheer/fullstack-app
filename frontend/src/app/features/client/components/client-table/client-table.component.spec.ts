import {createComponentFactory, Spectator} from '@ngneat/spectator';
import {ClientTableComponent} from './client-table.component';
import {ClientFacade} from '../../facade/client.facade';
import {ClientResponse, ClientsControllerService} from '../../../../../api';
import {signal} from '@angular/core';
import {MessageService} from 'primeng/api';
import {HttpClient} from '@angular/common/http';

describe('ClientTableComponent', () => {
  let spectator: Spectator<ClientTableComponent>;

  const sampleClients: ClientResponse[] = [
    {
      id: 1,
      fullName: "John Wick",
      displayName: "John W.",
      email: "John@wick.com",
      details: "Highly skilled assassin known for his expertise in hand-to-hand combat and strategic takedowns.",
      active: true,
      location: "New York, USA"
    },
    {
      id: 2,
      fullName: "John Snow",
      displayName: "John S.",
      email: "John@snow.com",
      details: "Military commander with leadership experience, skilled in battle tactics and diplomacy.",
      active: false,
      location: "The North, Westeros"
    },
  ];

  const mockFacade = {
    isLoading: signal(false),
    errorMessage: signal(''),
    originalClientList: signal<ClientResponse[]>(sampleClients),
    loadClients: jasmine.createSpy('loadClients'),
  } as unknown as ClientFacade;

  const createComponent = createComponentFactory({
    component: ClientTableComponent,
    mocks: [ClientsControllerService, MessageService, HttpClient],
    componentProviders: [
      {provide: ClientFacade, useValue: mockFacade}
    ],
    detectChanges: false,
  });

  beforeEach(() => {
    mockFacade.isLoading.set(false);
    mockFacade.errorMessage.set('');
    mockFacade.originalClientList.set(sampleClients);

    spectator = createComponent();
  });

  it('should create', () => {
    expect(spectator).toBeTruthy();
  })

  it('calls load clients on init and mirrors original list into filtered list', () => {
    spectator.detectChanges();
    expect(mockFacade.loadClients).toHaveBeenCalled();

    expect(spectator.component.filteredClientList()).toEqual(sampleClients);
  });

  it('shows progress spinner when loading', () => {
    mockFacade.isLoading.set(true);
    spectator.detectChanges();

    expect(spectator.query('p-progress-spinner')).toBeTruthy();
    expect(spectator.query('p-table')).toBeFalsy();
    expect(spectator.query('p-message')).toBeFalsy();
  });

  it('shows error message when there is an error', () => {
    mockFacade.errorMessage.set('Could not load clients');
    spectator.detectChanges();

    expect(spectator.query('p-message')).toBeTruthy();
    expect(spectator.query('p-table')).toBeFalsy();
    expect(spectator.query('p-progress-spinner')).toBeFalsy();
  });

  it('should contain two table rows', () => {
    spectator.detectChanges();
    const rows = spectator.queryAll('p-table tbody tr');
    expect(rows.length).toBe(2);
  });

  it('filters the table when typing in the search box', () => {
    spectator.detectChanges();

    const input = spectator.query<HTMLInputElement>('input[pInputText]');
    expect(input).toBeTruthy();

    spectator.typeInElement('John Wick', input!);

    const filtered = spectator.component.filteredClientList();
    expect(filtered.length).toBe(1);
    expect(filtered[0].fullName).toBe('John Wick');
  });

  it('opens the edit dialog on row select (DOM custom event)', () => {
    spectator.detectChanges();

    const tableEl = spectator.query('p-table') as HTMLElement;
    expect(tableEl).toBeTruthy();

    tableEl.dispatchEvent(new Event('onRowSelect'));
    spectator.detectChanges();

    expect(spectator.component.openEditClientPopup()).toBeTrue();
  });

  it('clears editClientValue when popup closes (effect-driven)', () => {
    spectator.component.editClientValue.set(sampleClients[0]);
    spectator.component.openEditClientPopup.set(true);
    spectator.detectChanges();

    spectator.component.openEditClientPopup.set(false);
    spectator.detectChanges();

    expect(spectator.component.editClientValue()).toBeUndefined();
  });

  it('should contain the client details component', () => {
    spectator.detectChanges();

    const details = spectator.query('app-clients-details');
    expect(details).toBeTruthy();
  });

  it('should contain the export button component', () => {
    spectator.detectChanges();
    const exportButton = spectator.query('app-export-button');
    expect(exportButton).toBeTruthy();
  });

  it('should contain the new client button component', () => {
    spectator.detectChanges();
    const newClientButton = spectator.query('app-new-client-button');
    expect(newClientButton).toBeTruthy();
  });
});
