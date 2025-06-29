import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-arendenbutton',
  imports: [],
  templateUrl: './arendenbutton.component.html',
  styleUrl: './arendenbutton.component.css'
})
export class ArendenbuttonComponent {
  @Output() arendenEvent = new EventEmitter();

  onClickedArenden() {
    this.arendenEvent.emit();
  }
}
