import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-fakturabutton',
  imports: [],
  templateUrl: './fakturabutton.component.html',
  styleUrl: './fakturabutton.component.css'
})
export class FakturabuttonComponent {

  @Output() fakturaEvent = new EventEmitter<boolean>();

  onClickFakturaEvent() {
    this.fakturaEvent.emit(true);
  }
}
