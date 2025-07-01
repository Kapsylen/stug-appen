import {Component} from '@angular/core';
import {Kontakt} from '../model/kontakt';
import {DUMMY_KONTAKTER} from '../../../assets/kontakter_data';
import {KontaktComponents} from './kontakt/kontakt.components';

@Component({
  selector: 'app-kontakter',
  imports: [KontaktComponents],
  templateUrl: './kontakter.component.html',
  styleUrl: './kontakter.component.css'
})
export class KontakterComponent {

  get kontakter(): Kontakt[] {
    return DUMMY_KONTAKTER;
  }
}
