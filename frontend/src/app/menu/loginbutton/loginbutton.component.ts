import {Component, EventEmitter, inject, Output} from '@angular/core';
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-loginbutton',
  imports: [],
  templateUrl: './loginbutton.component.html',
  styleUrl: './loginbutton.component.css'
})
export class LoginbuttonComponent {

  private readonly keycloak = inject(Keycloak);

  @Output() loginEvent = new EventEmitter();

  onClickedLogin() {
    this.keycloak.login();
    this.loginEvent.emit();
  }
}
