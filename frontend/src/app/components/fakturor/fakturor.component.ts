import {Component} from '@angular/core';
import {FakturaComponents} from './faktura/faktura.components';
import {FakturaService} from '../../service/faktura.service';
import {NewFakturaComponent} from './new-faktura/new-faktura.component';

@Component({
  selector: 'app-fakturor',
  imports: [
    FakturaComponents,
    NewFakturaComponent
  ],
  templateUrl: './fakturor.component.html',
  styleUrl: './fakturor.component.css'
})
export class FakturorComponent {

  isAddingFaktura = false;

  constructor(private fakturaService: FakturaService) {}

  get fakturor() {
   return this.fakturaService.getFakturor();
  }

  onStartAddFaktura() {
    this.isAddingFaktura = true;
  }

  onCloseAddFaktura() {
    this.isAddingFaktura = false;
  }
}
