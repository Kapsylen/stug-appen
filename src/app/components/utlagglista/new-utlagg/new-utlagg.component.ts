import {Component, EventEmitter, inject, Output} from '@angular/core';
import {UtlaggService} from '../../../service/utlagg.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-new-utlagg',
  imports: [
    FormsModule
  ],
  templateUrl: './new-utlagg.component.html',
  styleUrl: './new-utlagg.component.css'
})
export class NewUtlaggComponent {
  @Output () close = new EventEmitter<void>();
  enteredTitle = '';
  enteredDate = '';
  enteredDescription = '';
  enteredPrice = '';

  private utlaggService  = inject(UtlaggService);

  onCancel() {
    this.close.emit();
  }

  onSubmit()  {
    this.utlaggService.addUtlagg(
      {
        title: this.enteredTitle,
        description: this.enteredDescription,
        date: this.enteredDate,
        price: this.enteredPrice,
      },
    );
    this.close.emit();
  }
}
