import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {FakturaService} from '../../../service/faktura.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-new-faktura',
  imports: [
    FormsModule,
    DatePipe
  ],
  templateUrl: './new-faktura.component.html',
  styleUrl: './new-faktura.component.css'
})
export class NewFakturaComponent {

  @Output() close = new EventEmitter<void>();
  private fakturaService = inject(FakturaService);
  enteredCustomerName = '';
  enteredCreatedAt = '';
  enteredDueDate = '';
  enteredDescription = '';
  enteredQuantity = '';
  enteredPrice = '';
  enteredTotal = '';
  enteredTotalAmount = '';
  enteredStatus = '';

  onSubmit() {
    this.fakturaService.saveFaktura(
      {
        invoiceNumber: '',
        clientName: this.enteredCustomerName,
        issueDate: this.enteredCreatedAt,
        dueDate: this.enteredDueDate,
        items: [{
          description: this.enteredDescription,
          quantity: this.enteredQuantity,
          price: this.enteredPrice,
          total: this.enteredTotal
        }],
        totalAmount: this.enteredTotalAmount,
        status: this.enteredStatus
      });
    this.close.emit();
  }

  onCancel() {
    this.close.emit();
  }

  handleDueDateChange(event: string) {
    const date = new Date(event);
    this.enteredDueDate = date.toISOString();
  }
}
