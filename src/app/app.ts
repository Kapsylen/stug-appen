import { Component } from '@angular/core';
import { HeaderComponent } from "./header/header.component";
import {Utlagg} from './components/model/utlagg';
import {UtlaggbuttonComponent} from './components/buttons/utlaggbutton/utlaggbutton.component';
import {UtlaggComponent} from './components/utlagg/utlagg.component';
import {DUMMY_UTLAGG} from '../assets/utlagg_data';
import {FakturabuttonComponent} from './components/buttons/fakturabutton/fakturabutton.component';
import {DUMMY_FAKTUROR} from '../assets/fakturor_data';
import {Faktura} from './components/model/faktura';
import {FakturaComponent} from './components/faktura/faktura.component';
import {KontakterbuttonComponent} from './components/buttons/kontakterbutton/kontakterbutton.component';
import {Kontakt} from './components/model/kontakt';
import {DUMMY_KONTAKTER} from '../assets/kontakter_data';
import {KontakterComponent} from './components/kontakter/kontakter.component';
import {DUMMY_ARENDEN} from '../assets/arenden_data';
import {ArendenbuttonComponent} from './components/buttons/arendenbutton/arendenbutton.component';
import {ArendenComponent} from './components/arenden/arenden.component';
import {Arende} from './components/model/arenden';

@Component({
  selector: 'app-root',
  imports: [UtlaggbuttonComponent, UtlaggComponent, FakturabuttonComponent, FakturaComponent, KontakterbuttonComponent, KontakterComponent, ArendenbuttonComponent, ArendenComponent, HeaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  utlagg: Utlagg[] = [];
  fakturor: Faktura[] = [];
  kontakter: Kontakt[] = [];
  arenden: Arende[] = [];

  onClickedUtlagg() {
    this.clearDisplayedData();
    this.utlagg = DUMMY_UTLAGG;
  }

  onClickedFaktura() {
    this.clearDisplayedData();
    this.fakturor = DUMMY_FAKTUROR;
  }

  onClickKontakter() {
      this.clearDisplayedData();
      this.kontakter = DUMMY_KONTAKTER;
  }

  onClickedArenden() {
    this.clearDisplayedData();
    this.arenden = DUMMY_ARENDEN;
  }

  private clearDisplayedData() {
    if(this.utlagg.length > 0 || this.fakturor.length > 0 || this.kontakter.length > 0 || this.arenden.length > 0) {
      this.utlagg = [];
      this.fakturor = [];
      this.kontakter = [];
      this.arenden = [];
    }
  }
}
