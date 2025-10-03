import {NewClientButtonComponent} from './new-client-button.component';
import {createComponentFactory, Spectator} from '@ngneat/spectator';
import {Router} from '@angular/router';

describe('NewClientButtonComponent', () => {
  let spectator: Spectator<NewClientButtonComponent>;
  let router: jasmine.SpyObj<Router>;

  const createComponent = createComponentFactory({
    component: NewClientButtonComponent,
    mocks: [Router],
    shallow: true,
  });

  beforeEach(() => {
    spectator = createComponent();
    router = spectator.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(spectator.component).toBeTruthy();
  });

  it('should navigate to "new" on button click', () => {
    spectator.click('p-button');
    expect(router.navigate).toHaveBeenCalledWith(['new']);
  });
});
