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

@Component({
  selector: 'app-root',
  imports: [KontakterComponent, HeaderComponent, UtlaggbuttonComponent, FakturabuttonComponent, KontakterbuttonComponent, ArendenbuttonComponent, FakturorComponent, UtlaggComponent, ArendenComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  utlaggEventReceived?: boolean = false;
  fakturaEventReceived?: boolean = false;
  kontaktEventReceived?: boolean = false;
  arendeEventReceived?: boolean = false;

  onUtlaggEvent($event: boolean) {
    this.utlaggEventReceived = $event;
  }

  onFakturaEvent($event: boolean) {
    this.fakturaEventReceived = $event;
  }

  onKontakterEvent($event: boolean) {
    this.kontaktEventReceived = $event;
  }

  onArendenEvent($event: boolean) {
    this.arendeEventReceived = $event;
  }
}
