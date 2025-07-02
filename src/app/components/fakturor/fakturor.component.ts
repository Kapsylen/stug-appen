import {Component} from '@angular/core';
import {FakturaComponents} from './faktura/faktura.components';
import {FakturaService} from '../../service/faktura.service';

@Component({
  selector: 'app-fakturor',
  imports: [
    FakturaComponents
  ],
  templateUrl: './fakturor.component.html',
  styleUrl: './fakturor.component.css'
})
export class FakturorComponent {

  constructor(private fakturaService: FakturaService) {}

  get fakturor() {
   return this.fakturaService.getFakturor();
  }

}
