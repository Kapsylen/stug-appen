import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-utlaggbutton',
  imports: [],
  templateUrl: './utlaggbutton.component.html',
  styleUrl: './utlaggbutton.component.css'
})
export class UtlaggbuttonComponent {

  @Output() utlaggEvent = new EventEmitter();

  onClickedUtlaggEvent() {
    this.utlaggEvent.emit();
  }
}
