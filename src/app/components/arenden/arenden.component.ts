import {Component} from '@angular/core';
import {ArendeComponents} from './arende/arende.components';
import {DataService} from '../../service/data.service';

@Component({
  selector: 'app-arenden',
  imports: [ArendeComponents],
  templateUrl: './arenden.component.html',
  styleUrl: './arenden.component.css'
})
export class ArendenComponent {

  constructor(private dataService: DataService) {}

    get getArenden() {
      return this.dataService.getArenden();
    }
}
