import {Component, effect, inject} from '@angular/core';
import {HeaderComponent} from "./header/header.component";
import {UtlaggbuttonComponent} from './menu/utlaggbutton/utlaggbutton.component';
import {FakturabuttonComponent} from './menu/fakturabutton/fakturabutton.component';
import {KontakterComponent} from './components/kontakter/kontakter.component';
import {KontakterbuttonComponent} from './menu/kontakterbutton/kontakterbutton.component';
import {ArendenbuttonComponent} from './menu/arendenbutton/arendenbutton.component';
import {FakturorComponent} from './components/fakturor/fakturor.component';
import {UtlaggComponent} from './components/utlagglista/utlagg.component';
import {ArendenComponent} from './components/arenden/arenden.component';
import {Utlagg} from './model/utlagg';
import {Faktura} from './model/faktura';
import {Kontakt} from './model/kontakt';
import {Arende} from './model/arenden';
import {Homebutton} from './menu/homebutton/homebutton';
import {HomeComponent} from './components/home/home.component';
import {LoginbuttonComponent} from './menu/loginbutton/loginbutton.component';
import {LogoutbuttonComponent} from './menu/logoutbutton/logoutbutton.component';
import {KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs} from 'keycloak-angular';

@Component({
  selector: 'app-root',
  imports: [KontakterComponent,
    HeaderComponent,
    UtlaggbuttonComponent,
    FakturabuttonComponent,
    KontakterbuttonComponent,
    ArendenbuttonComponent,
    FakturorComponent,
    UtlaggComponent,
    ArendenComponent, Homebutton, HomeComponent, LoginbuttonComponent, LogoutbuttonComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  authenticated = false;
  keycloakStatus: string | undefined;
  private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

  constructor() {
    effect(() => {
      const keycloakEvent = this.keycloakSignal();

      this.keycloakStatus = keycloakEvent.type;

      if (keycloakEvent.type === KeycloakEventType.Ready) {
        this.authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args);
      }

      if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
        this.authenticated = false;
      }
    });
  }

  isHomeClicked = false;
  isUtlaggClicked = false;
  isFakturaClicked = false;
  isKontakterClicked = false;
  isArendenClicked = false;
  isLoginClicked = false;
  isLogoutClicked = false;

  utlagg: Utlagg[] = [];
  fakturor: Faktura[] = [];
  kontakter: Kontakt[] = [];
  arenden: Arende[] = [];

  onClickedHome() {
    this.resetClickedButtons();
    this.isHomeClicked = true;
  }

  async onClickedUtlagg() {
    this.resetClickedButtons();
    this.isUtlaggClicked = true;
  }

  onClickedFakturor() {
    this.resetClickedButtons();
    this.isFakturaClicked = true;
  }

  onClickedKontakter() {
    this.resetClickedButtons();
    this.isKontakterClicked = true;
  }

  onClickedArenden() {
    this.resetClickedButtons();
    this.isArendenClicked = true;
  }

  onClickedLogin() {
    this.resetClickedButtons();
    this.isLoginClicked = true;
  }

  onClickedLogout() {
    this.resetClickedButtons();
    this.isLogoutClicked = true;
  }

   private resetClickedButtons() {
    this.isHomeClicked = false;
    this.isUtlaggClicked = false;
    this.isFakturaClicked = false;
    this.isKontakterClicked = false;
    this.isArendenClicked = false;
    this.isLoginClicked = false;
    this.isLogoutClicked = false;
  }

}
