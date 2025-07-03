import {Component} from '@angular/core';
import {ArendeComponents} from './arende/arende.components';
import {ArendeService} from '../../service/arende.service';
import {NewArendeComponent} from './new-arende/new-arende.component';

@Component({
  selector: 'app-arenden',
  imports: [ArendeComponents, NewArendeComponent],
  templateUrl: './arenden.component.html',
  styleUrl: './arenden.component.css'
})
export class ArendenComponent {

  constructor(private arendeService: ArendeService) {}

  isAddingArende = false;

    get getArenden() {
      return this.arendeService.getArenden();
    }

  onCloseAddArende() {
    this.isAddingArende = false;
  }

  onAddArende() {
    this.isAddingArende = true;
  }
}
