import {Routes} from '@angular/router';
import {
  NewClientFormComponent
} from './features/client/components/new-client/new-client-form/new-client-form.component';
import {ClientsTableComponent} from './features/client/components/clients-table/clients-table.component';

export const routes: Routes = [
  {path: '', component: ClientsTableComponent},
  {path: 'new', component: NewClientFormComponent}
];
