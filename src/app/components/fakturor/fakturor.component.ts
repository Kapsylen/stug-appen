import {Component} from '@angular/core';
import {FakturaComponents} from './faktura/faktura.components';
import {DataService} from '../../service/data.service';

@Component({
  selector: 'app-fakturor',
  imports: [
    FakturaComponents
  ],
  templateUrl: './fakturor.component.html',
  styleUrl: './fakturor.component.css'
})
export class FakturorComponent {

  constructor(private dataService: DataService) {}

  get fakturor() {
   return this.dataService.getFakturor();
  }

}
