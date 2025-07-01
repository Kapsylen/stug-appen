import {Component, Input} from '@angular/core';
import {Utlagg} from '../../model/utlagg';
import {CardComponent} from '../../common/card/card.component';

@Component({
  selector: 'app-utlagg',
  imports: [
    CardComponent
  ],
  templateUrl: './utlagg.components.html',
  styleUrl: './utlagg.components.css'
})
export class UtlaggComponents {
  @Input({required: true}) utlagg!: Utlagg;
}
