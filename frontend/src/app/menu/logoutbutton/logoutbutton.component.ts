import {Component, EventEmitter, inject, Output} from '@angular/core';
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-logoutbutton',
  imports: [],
  templateUrl: './logoutbutton.component.html',
  styleUrl: './logoutbutton.component.css'
})
export class LogoutbuttonComponent {

  private readonly keycloak = inject(Keycloak);
  @Output() logoutEvent = new EventEmitter();

  onClickedLogout() {
    this.keycloak.logout();
    this.logoutEvent.emit();
  }
}
