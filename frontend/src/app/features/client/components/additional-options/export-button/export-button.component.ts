import {Component, inject} from '@angular/core';
import {MessageService} from 'primeng/api';
import {Button} from 'primeng/button';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-export-button',
  imports: [
    Button
  ],
  templateUrl: './export-button.component.html'
})
export class ExportButtonComponent {
  private readonly messageService: MessageService = inject(MessageService);
  private readonly http: HttpClient = inject(HttpClient);

  //todo fix or remove, this is a testproject, download works, but have to get it to work with openapi.
  exportClientCsv(fileName: string): void {
    this.http.get(`http://localhost:8080/clients/export`, {
      params: {fileName},
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        const blob = response.body!;
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = fileName + '.csv';
        a.click();
        window.URL.revokeObjectURL(url);
        this.addSuccessMessage();
      },
      error: () => {
        this.logErrorMessage();
      }
    })
  }

  private addSuccessMessage() {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: "The download should start soon!",
      key: 'bottom-right-toast',
      life: 5000
    });
  }

  private logErrorMessage() {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: "Exporting the client list is not possible",
      key: 'bottom-right-toast',
      life: 5000
    });
  }
}
