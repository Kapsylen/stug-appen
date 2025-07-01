import {Component} from '@angular/core';
import {UtlaggbuttonComponent} from './utlaggbutton/utlaggbutton.component';
import {FakturabuttonComponent} from './fakturabutton/fakturabutton.component';
import {KontakterbuttonComponent} from './kontakterbutton/kontakterbutton.component';
import {ArendenbuttonComponent} from './arendenbutton/arendenbutton.component';

@Component({
  selector: 'app-buttons',
  imports: [
    UtlaggbuttonComponent,
    FakturabuttonComponent,
    KontakterbuttonComponent,
    ArendenbuttonComponent
  ],
  templateUrl: './buttons.component.html',
  styleUrl: './buttons.component.css'
})
export class ButtonsComponent {
}
