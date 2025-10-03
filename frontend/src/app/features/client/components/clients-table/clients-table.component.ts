import {ChangeDetectionStrategy, Component, effect, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {ClientResponse} from '../../../../../api';
import {ProgressSpinner} from 'primeng/progressspinner';
import {Message} from 'primeng/message';
import {TableModule} from 'primeng/table';
import {Checkbox} from 'primeng/checkbox';
import {FormsModule} from '@angular/forms';
import {InputIcon} from 'primeng/inputicon';
import {IconField} from 'primeng/iconfield';
import {InputText} from 'primeng/inputtext';
import {ClientsDetailsComponent} from './clienst-details/clients-details.component';
import {ClientFacade} from '../../facade/client.facade';
import {NewClientButtonComponent} from '../new-client/new-client-button/new-client-button.component';

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
    InputText,
    ClientsDetailsComponent,
    NewClientButtonComponent
  ],
  templateUrl: './clients-table.component.html',
  providers: [ClientFacade]
})
export class ClientsTableComponent implements OnInit {
  readonly clientFacade: ClientFacade = inject(ClientFacade)

  readonly filteredClientList: WritableSignal<ClientResponse[]> = signal([])

  readonly openEditClientPopup: WritableSignal<boolean> = signal<boolean>(false);
  readonly editClientValue: WritableSignal<ClientResponse | undefined> = signal<ClientResponse | undefined>(undefined);

  constructor() {
    effect(() => {
      this.filteredClientList.set(this.clientFacade.originalClientList())
    });

    effect(() => {
      if (!this.openEditClientPopup()) {
        this.editClientValue.set(undefined)
      }
    });
  }

  ngOnInit(): void {
    this.clientFacade.loadClients();
  }

  filterTable(value: string) {
    const search = value.toLowerCase();
    const filtered = this.clientFacade.originalClientList().filter(client =>
      (client.fullName?.toLowerCase().includes(search) ?? false) ||
      (client.displayName?.toLowerCase().includes(search) ?? false) ||
      (client.email?.toLowerCase().includes(search) ?? false) ||
      (client.location?.toLowerCase().includes(search) ?? false)
    );
    this.filteredClientList.set(filtered);
  }

  openEditClientDetails() {
    this.openEditClientPopup.set(true)
  }
}
