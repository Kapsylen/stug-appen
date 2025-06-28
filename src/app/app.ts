import { Component } from '@angular/core';
import { HeaderComponent } from "./header/header.component";
import {Utlagg} from './components/model/utlagg';
import {UtlaggbuttonComponent} from './components/buttons/utlaggbutton/utlaggbutton.component';
import {UtlaggComponent} from './components/utlagg/utlagg.component';
import {DUMMY_UTLAGG} from '../assets/utlagg_data';

@Component({
  selector: 'app-root',
  imports: [HeaderComponent, UtlaggbuttonComponent, UtlaggComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  isClicked = false;

  utlagg: Utlagg[] = DUMMY_UTLAGG

  onClickedUtlagg($event: boolean) {
      if($event) {
        this.isClicked = true;
       }
  }
}
