import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-arendenbutton',
  imports: [],
  templateUrl: './arendenbutton.component.html',
  styleUrl: './arendenbutton.component.css'
})
export class ArendenbuttonComponent {
  @Output() arendenEvent = new EventEmitter<boolean>();
  @Output() isClicked = false;


  onClickedArenden() {
    this.isClicked = true;
    this.arendenEvent.emit(true);
  }
}
