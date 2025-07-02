import {Component} from '@angular/core';
import {HeaderComponent} from "./header/header.component";
import {UtlaggbuttonComponent} from './buttons/utlaggbutton/utlaggbutton.component';
import {FakturabuttonComponent} from './buttons/fakturabutton/fakturabutton.component';
import {KontakterComponent} from './components/kontakter/kontakter.component';
import {KontakterbuttonComponent} from './buttons/kontakterbutton/kontakterbutton.component';
import {ArendenbuttonComponent} from './buttons/arendenbutton/arendenbutton.component';
import {FakturorComponent} from './components/fakturor/fakturor.component';
import {UtlaggComponent} from './components/utlagglista/utlagg.component';
import {ArendenComponent} from './components/arenden/arenden.component';
import {DataService} from './service/data.service';
import {Utlagg} from './components/model/utlagg';
import {Faktura} from './components/model/faktura';
import {Kontakt} from './components/model/kontakt';
import {Arende} from './components/model/arenden';
import {DUMMY_UTLAGG} from '../assets/utlagg_data';
import {DUMMY_FAKTUROR} from '../assets/fakturor_data';
import {DUMMY_KONTAKTER} from '../assets/kontakter_data';
import {DUMMY_ARENDEN} from '../assets/arenden_data';

@Component({
  selector: 'app-root',
  imports: [KontakterComponent, HeaderComponent, UtlaggbuttonComponent, FakturabuttonComponent, KontakterbuttonComponent, ArendenbuttonComponent, FakturorComponent, UtlaggComponent, ArendenComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  isUtlaggClicked = false;
  isFakturaClicked = false;
  isKontakterClicked = false;
  isArendenClicked = false;

  utlagg : Utlagg[] = [];
  fakturor: Faktura[] = [];
  kontakter: Kontakt[] = [];
  arenden: Arende[] = [];


  onClickedUtlagg($event: boolean) {
    if($event) {
      this.clearDisplayedData();
      this.utlagg = DUMMY_UTLAGG;
      this.isUtlaggClicked = true;
    }
  }

  onClickedFaktura($event: boolean) {
    if($event) {
      this.clearDisplayedData();
      this.fakturor = DUMMY_FAKTUROR;
      this.isFakturaClicked = true;
    }
  }

  onClickedKontakter($event: boolean) {
    if($event) {
      this.clearDisplayedData();
      this.kontakter = DUMMY_KONTAKTER;
      this.isKontakterClicked = true;
    }
  }
  onClickedArenden($event: boolean) {
    if($event) {
      this.clearDisplayedData();
      this.arenden = DUMMY_ARENDEN;
      this.isArendenClicked = true;
    }
  }


  private clearDisplayedData() {
    if(this.utlagg.length > 0 || this.fakturor.length > 0 || this.kontakter.length > 0 || this.arenden.length > 0) {
      this.isUtlaggClicked = false;
      this.isFakturaClicked = false;
      this.isKontakterClicked = false;
      this.isArendenClicked = false;
      this.utlagg = [];
      this.fakturor = [];
      this.kontakter = [];
      this.arenden = [];
    }
  }
}
