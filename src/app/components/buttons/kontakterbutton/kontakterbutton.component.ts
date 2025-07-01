import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-kontakterbutton',
  imports: [],
  templateUrl: './kontakterbutton.component.html',
  styleUrl: './kontakterbutton.component.css'
})
export class KontakterbuttonComponent {

  @Output() kontaktEvent = new EventEmitter<boolean>();

  onClickedKontakter() {
    this.kontaktEvent.emit(true);
  }
}
