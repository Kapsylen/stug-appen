import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Kontakt} from '../../model/kontakt';

@Component({
  selector: 'app-kontakt',
  imports: [
    CardComponent
  ],
  templateUrl: './kontakt.components.html',
  styleUrl: './kontakt.components.css'
})
export class KontaktComponents {
  @Input() kontakt!: Kontakt;
}
