import {ChangeDetectionStrategy, Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {ClientResponse, ClientsControllerService} from '../../../../../api';
import {ProgressSpinner} from 'primeng/progressspinner';
import {Message} from 'primeng/message';
import {TableModule} from 'primeng/table';
import {Checkbox} from 'primeng/checkbox';
import {FormsModule} from '@angular/forms';
import {InputIcon} from 'primeng/inputicon';
import {IconField} from 'primeng/iconfield';
import {InputText} from 'primeng/inputtext';

@Component({
  selector: 'app-clients-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ProgressSpinner,
    Message,
    TableModule,
    Checkbox,
    FormsModule,
    InputIcon,
    IconField,
    InputText
  ],
  templateUrl: './clients-table.component.html'
})
export class ClientsTableComponent implements OnInit {
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  readonly isLoading: WritableSignal<boolean> = signal(true);
  readonly errorMessage: WritableSignal<string> = signal('');

  private readonly originalClientList: WritableSignal<ClientResponse[]> = signal([])
  readonly filteredClientList: WritableSignal<ClientResponse[]> = signal([])

  ngOnInit(): void {
    this.clientsService.getClients().subscribe({
        next: (clients: ClientResponse[]) => {
          this.isLoading.set(false);
          this.originalClientList.set(clients);
          this.filteredClientList.set(clients);
        },
        error: () => {
          this.errorMessage.set("Something went wrong with loading the clients.");
          this.isLoading.set(false);
        }
      }
    )
  }

  filterTable(value: string) {
    const search = value.toLowerCase();
    const filtered = this.originalClientList().filter(client =>
      (client.fullName?.toLowerCase().includes(search) ?? false) ||
      (client.displayName?.toLowerCase().includes(search) ?? false) ||
      (client.email?.toLowerCase().includes(search) ?? false) ||
      (client.location?.toLowerCase().includes(search) ?? false)
    );
    this.filteredClientList.set(filtered);
  }
}
