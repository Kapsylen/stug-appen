import {Component} from '@angular/core';
import {Kontakt} from '../../model/kontakt';
import {KontaktComponents} from './kontakt/kontakt.components';
import {KontaktService} from '../../service/kontakt.service';
import {NewKontaktComponent} from './new-kontakt/new-kontakt.component';

@Component({
  selector: 'app-kontakter',
  imports: [KontaktComponents, NewKontaktComponent],
  templateUrl: './kontakter.component.html',
  styleUrl: './kontakter.component.css'
})
export class KontakterComponent {

  isAddingKontakt = false;

  constructor(private kontaktService: KontaktService) {}

  get kontakter() {
    return this.kontaktService.getKontakter();
  }

  onCloseAddKontakt() {
    this.isAddingKontakt = false;
  }

  onStartAddKontakt() {
    this.isAddingKontakt = true;
  }
}
