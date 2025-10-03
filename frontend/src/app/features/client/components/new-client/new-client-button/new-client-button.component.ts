import {Component, inject} from '@angular/core';
import {Button} from 'primeng/button';
import {Router} from '@angular/router';

@Component({
  selector: 'app-new-client-button',
  imports: [
    Button
  ],
  templateUrl: './new-client-button.component.html'
})
export class NewClientButtonComponent {
  private readonly router: Router = inject(Router);

  navigateToNewClientForm(): void {
    this.router.navigate(['new']);
  }
}
