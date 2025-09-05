import {Component} from '@angular/core';
import {UtlaggbuttonComponent} from './utlaggbutton/utlaggbutton.component';
import {FakturabuttonComponent} from './fakturabutton/fakturabutton.component';
import {KontakterbuttonComponent} from './kontakterbutton/kontakterbutton.component';
import {ArendenbuttonComponent} from './arendenbutton/arendenbutton.component';
import {RouterLink, RouterModule} from '@angular/router';
import {HasRolesDirective} from 'keycloak-angular';

@Component({
  selector: 'app-menu',
  imports: [
    UtlaggbuttonComponent,
    FakturabuttonComponent,
    KontakterbuttonComponent,
    ArendenbuttonComponent,
    RouterLink,
    RouterModule,
    HasRolesDirective,
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {

}
