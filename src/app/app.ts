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
import {Utlagg} from './model/utlagg';
import {Faktura} from './model/faktura';
import {Kontakt} from './model/kontakt';
import {Arende} from './model/arenden';
import {UtlaggService} from './service/utlagg.service';
import {KontaktService} from './service/kontakt.service';
import {ArendeService} from './service/arende.service';
import {FakturaService} from './service/faktura.service';

@Component({
  selector: 'app-root',
  imports: [KontakterComponent, HeaderComponent, UtlaggbuttonComponent, FakturabuttonComponent, KontakterbuttonComponent, ArendenbuttonComponent, FakturorComponent, UtlaggComponent, ArendenComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  constructor(private utlaggService: UtlaggService, private fakturaServic: FakturaService, private kontakterService: KontaktService, private arendeService: ArendeService) {}

  isUtlaggClicked = false;
  isFakturaClicked = false;
  isKontakterClicked = false;
  isArendenClicked = false;

  utlagg: Utlagg[] = [];
  fakturor: Faktura[] = [];
  kontakter: Kontakt[] = [];
  arenden: Arende[] = [];


  onClickedUtlagg() {
    this.resetClickedButtons();
    this.utlagg = this.utlaggService.getUtlagg();
    this.isUtlaggClicked = true;
  }

  onClickedFakturor() {
    this.resetClickedButtons();
    this.fakturor = this.fakturaServic.getFakturor();
    this.isFakturaClicked = true;
  }

  onClickedKontakter() {
    this.resetClickedButtons();
    this.kontakter = this.kontakterService.getKontakter();
    this.isKontakterClicked = true;
  }

  onClickedArenden() {
    this.resetClickedButtons();
    this.arenden = this.arendeService.getArenden();
    this.isArendenClicked = true;
  }

   private resetClickedButtons() {
    this.isUtlaggClicked = false;
    this.isFakturaClicked = false;
    this.isKontakterClicked = false;
    this.isArendenClicked = false;
  }
}
