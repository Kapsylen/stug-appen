import {Component} from '@angular/core';
import {FakturaComponents} from './faktura/faktura.components';
import {Faktura} from '../model/faktura';
import {DUMMY_FAKTUROR} from '../../../assets/fakturor_data';

@Component({
  selector: 'app-fakturor',
  imports: [
    FakturaComponents
  ],
  templateUrl: './fakturor.component.html',
  styleUrl: './fakturor.component.css'
})
export class FakturorComponent {

  get fakturor(): Faktura[] {
    return DUMMY_FAKTUROR;
  }

}
