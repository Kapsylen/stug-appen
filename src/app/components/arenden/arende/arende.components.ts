import {Component, Input} from '@angular/core';
import {Arende} from '../../model/arenden';
import {CardComponent} from '../../common/card/card.component';

@Component({
  selector: 'app-arende',
  imports: [
    CardComponent
  ],
  templateUrl: './arende.components.html',
  styleUrl: './arende.components.css'
})
export class ArendeComponents {
  @Input() arende!: Arende;
}
