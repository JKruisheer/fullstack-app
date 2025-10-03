import {Routes} from '@angular/router';
import {ClientComponent} from './features/client/client.component';
import {
  NewClientFormComponent
} from './features/client/components/new-client/new-client-form/new-client-form.component';

export const routes: Routes = [
  {path: '', component: ClientComponent},
  {path: 'new', component: NewClientFormComponent}
];
