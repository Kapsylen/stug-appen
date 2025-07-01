import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Arende} from '../model/arenden';
import {ArendeComponents} from './arende/arende.components';
import {DUMMY_ARENDEN} from '../../../assets/arenden_data';
import {ArendenbuttonComponent} from '../buttons/arendenbutton/arendenbutton.component';

@Component({
  selector: 'app-arenden',
  imports: [ArendeComponents],
  templateUrl: './arenden.component.html',
  styleUrl: './arenden.component.css'
})
export class ArendenComponent {
  get arenden() {
    return DUMMY_ARENDEN;
  }
}
