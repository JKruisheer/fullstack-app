import {Component, effect, inject, input, InputSignal, model, ModelSignal} from '@angular/core';
import {Dialog} from 'primeng/dialog';
import {Button} from 'primeng/button';
import {ClientResponse, ClientsControllerService, PatchClientRequest} from '../../../../../../api';
import {Textarea} from 'primeng/textarea';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Checkbox} from 'primeng/checkbox';
import {InputText} from 'primeng/inputtext';
import {Message} from 'primeng/message';
import {ClientFacade} from '../../../facade/client.facade';

export interface ClientDetailsForm {
  displayName: FormControl<string>;
  details: FormControl<string | undefined>;
  active: FormControl<boolean>;
  location: FormControl<string | undefined>;
}

@Component({
  selector: 'app-clients-details',
  imports: [
    Dialog,
    Button,
    Textarea,
    FormsModule,
    ReactiveFormsModule,
    Checkbox,
    InputText,
    Message
  ],
  templateUrl: './clients-details.component.html'
})
export class ClientsDetailsComponent {
  private readonly clientFacade: ClientFacade = inject(ClientFacade);
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  clientDetailsVisible: ModelSignal<boolean> = model.required<boolean>();
  clientDetails: InputSignal<ClientResponse | undefined> = input.required<ClientResponse | undefined>();

  editClientForm: FormGroup<ClientDetailsForm> = new FormGroup<ClientDetailsForm>({
    displayName: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    details: new FormControl(undefined, {nonNullable: true,}),
    active: new FormControl(false, {nonNullable: true, validators: [Validators.required]}),
    location: new FormControl(undefined, {nonNullable: true}),
  });

  constructor() {
    effect(() => {
      const clientResponse = this.clientDetails();
      if (clientResponse) {
        this.editClientForm.patchValue({
          displayName: clientResponse.displayName,
          details: clientResponse.details,
          active: clientResponse.active,
          location: clientResponse.location
        })
      }
    });
  }

  saveClient(): void {
    if (!this.editClientForm.valid) {
      this.editClientForm.markAllAsTouched();
      return;
    }

    const patchClientRequest: PatchClientRequest = {
      displayName: this.editClientForm.controls.displayName.value,
      details: this.editClientForm.controls.details.value,
      active: this.editClientForm.controls.active.value,
      location: this.editClientForm.controls.location.value,
    }

    this.clientsService.updateClient(this.clientDetails()!.id, patchClientRequest).subscribe({
      next: () => {
        this.clientFacade.loadClients();
        this.clientDetailsVisible.set(false);
      }
    })
  }
}
