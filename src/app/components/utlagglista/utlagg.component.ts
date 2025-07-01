import {Component} from '@angular/core';
import {UtlaggComponents} from './utlagg/utlagg.components';
import {DUMMY_UTLAGG} from '../../../assets/utlagg_data';

@Component({
  selector: 'app-utlagglista',
  imports: [UtlaggComponents],
  templateUrl: './utlagg.component.html',
  styleUrl: './utlagg.component.css'
})
export class UtlaggComponent {

  get utlagg() {
    return DUMMY_UTLAGG;
  }
}
