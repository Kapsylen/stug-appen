import {Component, EventEmitter, inject, Output} from '@angular/core';
import {UtlaggService} from '../../../service/utlagg.service';
import {FormsModule} from '@angular/forms';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-new-utlagg',
  imports: [
    FormsModule,
    DatePipe
  ],
  templateUrl: './new-utlagg.component.html',
  styleUrl: './new-utlagg.component.css'
})
export class NewUtlaggComponent {
  @Output () close = new EventEmitter<void>();
  private utlaggService  = inject(UtlaggService);
  enteredTitle = '';
  enteredOutlayTime = '';
  enteredDescription = '';
  enteredPrice = '';


  onSubmit()  {
    this.utlaggService.saveUtlagg(
      {
        title: this.enteredTitle,
        description: this.enteredDescription,
        outlayDate: this.enteredOutlayTime,
        price: this.enteredPrice,
      },
    );
    this.close.emit();
  }

  onCancel() {
    this.close.emit();
  }

  enteredOutlayTimeChange(event: string) {
    const date = new Date(event);
    this.enteredOutlayTime = date.toISOString();
  }
}
