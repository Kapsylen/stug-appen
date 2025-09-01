import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-homebutton',
  imports: [],
  templateUrl: './homebutton.html',
  styleUrl: './homebutton.css'
})
export class Homebutton {
  @Output() homeEvent = new EventEmitter();

  onClickHomeEvent() {
    this.homeEvent.emit();
  }
}
