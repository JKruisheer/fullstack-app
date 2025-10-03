import {Component, effect, inject, signal, WritableSignal} from '@angular/core';
import {Button} from 'primeng/button';
import {Location} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Message} from 'primeng/message';
import {InputText} from 'primeng/inputtext';
import {Checkbox} from 'primeng/checkbox';
import {Textarea} from 'primeng/textarea';
import {ClientsControllerService, NewClientRequest, Problem} from '../../../../../../api';
import {toSignal} from '@angular/core/rxjs-interop';

export interface NewClientForm {
  fullName: FormControl<string>;
  displayName: FormControl<string>;
  email: FormControl<string>;
  details: FormControl<string | undefined>;
  active: FormControl<boolean>;
  location: FormControl<string | undefined>;
}

@Component({
  selector: 'app-new-client-form',
  imports: [
    Button,
    Message,
    ReactiveFormsModule,
    InputText,
    Checkbox,
    Textarea
  ],
  templateUrl: './new-client-form.component.html'
})
export class NewClientFormComponent {
  private readonly clientsService: ClientsControllerService = inject(ClientsControllerService);
  private readonly location: Location = inject(Location);

  readonly saveClientErrorMessage: WritableSignal<string> = signal<string>('');

  newClientForm: FormGroup<NewClientForm> = new FormGroup<NewClientForm>({
    fullName: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    displayName: new FormControl('', {nonNullable: true, validators: [Validators.required]}),
    email: new FormControl('', {nonNullable: true, validators: [Validators.required, Validators.email]}),
    details: new FormControl(undefined, {nonNullable: true,}),
    active: new FormControl(false, {nonNullable: true, validators: [Validators.required]}),
    location: new FormControl(undefined, {nonNullable: true}),
  });

  readonly clientFormChanges = toSignal(this.newClientForm.controls.email.valueChanges);

  constructor() {
    effect(() => {
      this.clientFormChanges()
      this.saveClientErrorMessage.set('')
    });
  }


  navigateBack() {
    this.location.back();
  }

  saveClient() {
    if (!this.newClientForm.valid) {
      this.newClientForm.markAllAsTouched();
      return;
    }

    const formControls = this.newClientForm.controls

    const newClientRequest: NewClientRequest = {
      fullName: formControls.fullName.value,
      displayName: formControls.displayName.value,
      email: formControls.email.value,
      details: formControls.details.value,
      active: formControls.active.value,
      location: formControls.location.value,
    }

    this.clientsService.createClient(newClientRequest).subscribe({
      next: () => {
        this.navigateBack();
      },
      error: (err) => {
        const errMessage = err.error as Problem;
        if (errMessage.translation) {
          this.saveClientErrorMessage.set(errMessage.translation);
        } else {
          this.saveClientErrorMessage.set("Something went wrong with saving the client");
        }
      }
    })
  }
}
