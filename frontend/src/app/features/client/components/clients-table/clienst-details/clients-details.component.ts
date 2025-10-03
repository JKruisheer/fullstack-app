import {Component, effect, input, InputSignal, model, ModelSignal} from '@angular/core';
import {Dialog} from 'primeng/dialog';
import {Button} from 'primeng/button';
import {ClientResponse} from '../../../../../../api';
import {Textarea} from 'primeng/textarea';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Checkbox} from 'primeng/checkbox';
import {InputText} from 'primeng/inputtext';
import {Message} from 'primeng/message';

export interface ClientDetailsForm {
  displayName: FormControl<string>;
  details: FormControl<string>;
  active: FormControl<boolean>;
  location: FormControl<string>;
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
  clientDetailsVisible: ModelSignal<boolean> = model.required<boolean>();
  clientDetails: InputSignal<ClientResponse | undefined> = input.required<ClientResponse | undefined>();

  editClientForm: FormGroup<ClientDetailsForm> = new FormGroup<ClientDetailsForm>({
    displayName: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    details: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    active: new FormControl(false, {nonNullable: true, validators: [Validators.required]}),
    location: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
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
}
