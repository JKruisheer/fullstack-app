import {Routes} from '@angular/router';
import {
  NewClientFormComponent
} from './features/client/components/new-client/new-client-form/new-client-form.component';
import {ClientTableComponent} from './features/client/components/client-table/client-table.component';

export const routes: Routes = [
  {path: '', component: ClientTableComponent},
  {path: 'new', component: NewClientFormComponent}
];
