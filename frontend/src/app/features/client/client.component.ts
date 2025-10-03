import {Component} from '@angular/core';
import {ClientsTableComponent} from './components/clients-table/clients-table.component';

@Component({
  selector: 'app-client',
  imports: [
    ClientsTableComponent
  ],
  templateUrl: './client.component.html'
})
export class ClientComponent {

}
