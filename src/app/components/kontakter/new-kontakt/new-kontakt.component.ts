import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {KontaktService} from '../../../service/kontakt.service';

@Component({
  selector: 'app-new-kontakt',
    imports: [
        FormsModule
    ],
  templateUrl: './new-kontakt.component.html',
  styleUrl: './new-kontakt.component.css'
})
export class NewKontaktComponent {
  @Output() close = new EventEmitter<void>();
  kontakterService = inject(KontaktService);

  enteredName = '';
  enteredCompany = '';
  enteredCategory = '';
  enteredPhone = '';
  enteredEmail = '';
  enteredAddress = '';
  enteredNotes = '';
  enteredStatus = ''

  onSubmit() {
    this.kontakterService.addKontak({
      name: this.enteredName,
      company: this.enteredCompany,
      category: this.enteredCategory,
      phone: this.enteredPhone,
      email: this.enteredEmail,
      address: this.enteredAddress,
      notes: this.enteredNotes,
      status: this.enteredStatus,
    });
    this.close.emit();
  }

  onCancel() {
    this.close.emit();
  }
}
