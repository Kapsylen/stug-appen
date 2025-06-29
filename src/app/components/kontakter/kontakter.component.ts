import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Kontakt} from '../model/kontakt';

@Component({
  selector: 'app-kontakter',
  imports: [
    CardComponent
  ],
  templateUrl: './kontakter.component.html',
  styleUrl: './kontakter.component.css'
})
export class KontakterComponent {
  @Input() kontakt!: Kontakt;
}
