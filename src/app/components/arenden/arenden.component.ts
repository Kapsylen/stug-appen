import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Arende} from '../model/arenden';

@Component({
  selector: 'app-arenden',
  imports: [
    CardComponent
  ],
  templateUrl: './arenden.component.html',
  styleUrl: './arenden.component.css'
})
export class ArendenComponent {

  @Input() arende!: Arende;

}
