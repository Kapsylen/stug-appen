import {Component} from '@angular/core';
import {ArendeComponents} from './arende/arende.components';
import {ArendeService} from '../../service/arende.service';

@Component({
  selector: 'app-arenden',
  imports: [ArendeComponents],
  templateUrl: './arenden.component.html',
  styleUrl: './arenden.component.css'
})
export class ArendenComponent {

  constructor(private arendeService: ArendeService) {}

    get getArenden() {
      return this.arendeService.getArenden();
    }
}
