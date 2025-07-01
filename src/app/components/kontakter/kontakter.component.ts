import {Component} from '@angular/core';
import {Kontakt} from '../model/kontakt';
import {KontaktComponents} from './kontakt/kontakt.components';
import {DataService} from '../../service/data.service';

@Component({
  selector: 'app-kontakter',
  imports: [KontaktComponents],
  templateUrl: './kontakter.component.html',
  styleUrl: './kontakter.component.css'
})
export class KontakterComponent {

  constructor(private dataService: DataService) {}

  get kontakter(): Kontakt[] {
    return this.dataService.getKontakter();
  }
}
