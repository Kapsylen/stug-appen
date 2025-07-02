import {Component} from '@angular/core';
import {Kontakt} from '../model/kontakt';
import {KontaktComponents} from './kontakt/kontakt.components';
import {KontaktService} from '../../service/kontakt.service';

@Component({
  selector: 'app-kontakter',
  imports: [KontaktComponents],
  templateUrl: './kontakter.component.html',
  styleUrl: './kontakter.component.css'
})
export class KontakterComponent {

  constructor(private kontaktService: KontaktService) {}

  get kontakter(): Kontakt[] {
    return this.kontaktService.getKontakter();
  }
}
