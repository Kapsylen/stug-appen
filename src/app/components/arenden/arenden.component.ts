import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ArendeComponents} from './arende/arende.components';
import {DUMMY_ARENDEN} from '../../../assets/arenden_data';

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
