import {ChangeDetectionStrategy, Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {ClientResponse, ClientsControllerService} from '../../../../../api';
import {ProgressSpinner} from 'primeng/progressspinner';
import {Message} from 'primeng/message';
import {TableModule} from 'primeng/table';
import {Checkbox} from 'primeng/checkbox';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-clients-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ProgressSpinner,
    Message,
    TableModule,
    Checkbox,
    FormsModule
  ],
  templateUrl: './clients-table.component.html'
})
export class ClientsTableComponent implements OnInit {
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  readonly isLoading: WritableSignal<boolean> = signal(true);
  readonly clients: WritableSignal<ClientResponse[]> = signal([])
  readonly errorMessage: WritableSignal<string> = signal('');

  ngOnInit(): void {
    this.clientsService.getClients().subscribe({
        next: (clients: ClientResponse[]) => {
          this.isLoading.set(false);
          this.clients.set(clients);
        },
        error: (err) => {
          this.errorMessage.set("Something went wrong with loading the clients.");
          this.isLoading.set(false);
        }
      }
    )
  }
}
