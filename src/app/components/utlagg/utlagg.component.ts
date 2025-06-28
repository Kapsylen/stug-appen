import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Utlagg} from '../model/utlagg';

@Component({
  selector: 'app-utlagg',
  imports: [CardComponent],
  templateUrl: './utlagg.component.html',
  styleUrl: './utlagg.component.css'
})
export class UtlaggComponent {
  @Input({required: true}) utlagg!: Utlagg;

}
