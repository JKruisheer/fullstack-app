import {Component, effect, inject, input, InputSignal, model, ModelSignal} from '@angular/core';
import {Dialog} from 'primeng/dialog';
import {Button} from 'primeng/button';
import {ClientResponse, ClientsControllerService, PatchClientRequest, Problem} from '../../../../../../api';
import {Textarea} from 'primeng/textarea';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Checkbox} from 'primeng/checkbox';
import {InputText} from 'primeng/inputtext';
import {Message} from 'primeng/message';
import {ClientFacade} from '../../../facade/client.facade';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ConfirmPopup} from 'primeng/confirmpopup';

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
    Message,
    ConfirmPopup
  ],
  templateUrl: './client-details.component.html',
  providers: [ConfirmationService]
})
export class ClientDetailsComponent {
  private readonly clientFacade: ClientFacade = inject(ClientFacade);
  private readonly confirmationService: ConfirmationService = inject(ConfirmationService);
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  private readonly messageService: MessageService = inject(MessageService);

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

    const formControls = this.editClientForm.controls

    const patchClientRequest: PatchClientRequest = {
      displayName: formControls.displayName.value,
      details: formControls.details.value,
      active: formControls.active.value,
      location: formControls.location.value,
    }

    this.clientsService.updateClient(this.clientDetails()!.id, patchClientRequest).subscribe({
      next: () => {
        this.clientFacade.loadClients();
        this.clientDetailsVisible.set(false);
      },
      error: (err) => {
        const problem = err.error as Problem;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: problem.translation ?? "Unknown problem",
          key: 'bottom-right-toast',
          life: 5000
        });
      }
    })
  }

  deleteClient(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to delete this client?',
      accept: () => {
        this.clientsService.deleteClientById(this.clientDetails()!.id).subscribe({
          next: () => {
            this.clientFacade.loadClients();
            this.clientDetailsVisible.set(false);
          },
          error: (err) => {
            const problem = err.error as Problem;
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: problem.translation ?? "Unknown problem",
              key: 'bottom-right-toast',
              life: 5000
            });
          }
        })
      }
    });
  }
}
