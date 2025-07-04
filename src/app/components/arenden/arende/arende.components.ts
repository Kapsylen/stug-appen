import {Component, inject, Input} from '@angular/core';
import {Arende} from '../../../model/arenden';
import {CardComponent} from '../../common/card/card.component';
import {ArendeService} from '../../../service/arende.service';

@Component({
  selector: 'app-arende',
  imports: [
    CardComponent
  ],
  templateUrl: './arende.components.html',
  styleUrl: './arende.components.css'
})
export class ArendeComponents {
  @Input() arende!: Arende;
  arendeService = inject(ArendeService);

  onDeleteArende() {
    this.arendeService.deleteArenden(this.arende.id);
  }

  onEditArende() {
    console.log(this.arende);
  }

  onResolved() {
    console.log(this.arende);
  }
}
