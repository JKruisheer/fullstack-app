import {ClientResponse, ClientsControllerService} from '../../../../api';
import {inject, signal, WritableSignal} from '@angular/core';

export class ClientFacade {
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  public readonly isLoading: WritableSignal<boolean> = signal(true);
  public readonly errorMessage: WritableSignal<string> = signal('');

  public readonly originalClientList: WritableSignal<ClientResponse[]> = signal([])

  loadClients(): void {
    this.clientsService.getClients().subscribe({
        next: (clients: ClientResponse[]) => {
          this.isLoading.set(false);
          this.originalClientList.set(clients);
        },
        error: () => {
          this.errorMessage.set("Something went wrong with loading the clients.");
          this.isLoading.set(false);
        }
      }
    )
  }
}
