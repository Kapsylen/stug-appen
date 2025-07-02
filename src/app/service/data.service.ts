import {Injectable} from '@angular/core';
import {Arende} from '../components/model/arenden';
import {DUMMY_ARENDEN} from '../../assets/arenden_data';
import {Faktura} from '../components/model/faktura';
import {Kontakt} from '../components/model/kontakt';
import {Utlagg} from '../components/model/utlagg';
import {DUMMY_FAKTUROR} from '../../assets/fakturor_data';
import {DUMMY_KONTAKTER} from '../../assets/kontakter_data';
import {DUMMY_UTLAGG} from '../../assets/utlagg_data';

@Injectable({ providedIn: 'root' })
export class DataService {

  private arenden : Arende[] = [];
  private fakturor : Faktura[] = [];
  private kontakter : Kontakt[] = [];
  private utlagg : Utlagg[] = [];

  getArenden() {
    this.clearDisplayedData();
    this.arenden = DUMMY_ARENDEN;
    return this.arenden;
  }

  getFakturor() {
    this.clearDisplayedData();
    this.fakturor = DUMMY_FAKTUROR;
    return this.fakturor;
  }

  getKontakter() {
    this.clearDisplayedData();
    this.kontakter = DUMMY_KONTAKTER;
    return this.kontakter;
  }

  getUtlagg() {
    this.clearDisplayedData();
    this.utlagg = DUMMY_UTLAGG;
    return this.utlagg;
  }

  clearDisplayedData() {
    if (this.utlagg.length > 0 || this.fakturor.length > 0 || this.kontakter.length > 0 || this.arenden.length > 0) {
      this.utlagg = [];
      this.fakturor = [];
      this.kontakter = [];
      this.arenden = [];
    }
  }
}
