import {NewClientFormComponent} from './new-client-form.component';
import {Observable, of, throwError} from 'rxjs';
import {createComponentFactory, Spectator} from '@ngneat/spectator';
import {ClientsControllerService} from '../../../../../../api';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpEvent} from '@angular/common/http';
import {Location} from '@angular/common';

describe('NewClientFormComponent', () => {
  let spectator: Spectator<NewClientFormComponent>;
  let clientsServiceMock: jasmine.SpyObj<ClientsControllerService>;
  let locationMock: jasmine.SpyObj<Location>;

  const createComponent = createComponentFactory({
    component: NewClientFormComponent,
    imports: [ReactiveFormsModule],
    providers: [
      {
        provide: ClientsControllerService,
        useValue: jasmine.createSpyObj('ClientsControllerService', ['createClient'])
      },
      {
        provide: Location,
        useValue: jasmine.createSpyObj('Location', ['back'])
      }
    ]
  });

  beforeEach(() => {
    spectator = createComponent();
    clientsServiceMock = spectator.inject(ClientsControllerService) as jasmine.SpyObj<ClientsControllerService>;
    locationMock = spectator.inject(Location) as jasmine.SpyObj<Location>;
    clientsServiceMock.createClient.and.returnValue(
      of({}) as unknown as Observable<HttpEvent<object>>
    );

    clientsServiceMock.createClient.calls.reset();

  });

  it('should create the component', () => {
    expect(spectator.component).toBeTruthy();
  });

  it('should mark all fields as touched if form is invalid on save', () => {
    spectator.component.newClientForm.patchValue({
      fullName: '',
      displayName: '',
      email: '',
      active: false
    });


    spectator.component.saveClient();
    expect(spectator.component.newClientForm.touched).toBeTrue();
    expect(spectator.component.newClientForm.get('fullName')?.touched).toBeTrue();
    expect(spectator.component.newClientForm.get('displayName')?.touched).toBeTrue();
    expect(spectator.component.newClientForm.get('email')?.touched).toBeTrue();

    expect(clientsServiceMock.createClient).not.toHaveBeenCalled();
  });

  it('should call createClient and navigate back on successful save', () => {
    fillClientForm();
    spectator.component.saveClient();
    expect(clientsServiceMock.createClient).toHaveBeenCalled();
    expect(locationMock.back).toHaveBeenCalled();
  });

  it('should set error message on failed save with translation', () => {
    fillClientForm()
    clientsServiceMock.createClient.and.returnValue(throwError(() => ({
      error: {translation: 'Translated error'}
    })));
    spectator.component.saveClient();
    expect(spectator.component.saveClientErrorMessage()).toBe('Translated error');
  });

  it('should set default error message on failed save without translation', () => {
    fillClientForm()
    clientsServiceMock.createClient.and.returnValue(throwError(() => ({
      error: {}
    })));
    spectator.component.saveClient();
    expect(spectator.component.saveClientErrorMessage()).toBe('Something went wrong with saving the client');
  });

  it('should call navigateBack when navigateBack is called', () => {
    spectator.component.navigateBack();
    expect(locationMock.back).toHaveBeenCalled();
  });

  function fillClientForm() {
    spectator.component.newClientForm.setValue({
      fullName: 'John Doe',
      displayName: 'JD',
      email: 'john@example.com',
      details: 'Some details',
      active: true,
      location: 'Amsterdam'
    });
  }
});
