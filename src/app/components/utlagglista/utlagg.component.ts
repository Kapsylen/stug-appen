import {Component} from '@angular/core';
import {UtlaggComponents} from './utlagg/utlagg.components';
import {UtlaggService} from '../../service/utlagg.service';

@Component({
  selector: 'app-utlagglista',
  imports: [UtlaggComponents],
  templateUrl: './utlagg.component.html',
  styleUrl: './utlagg.component.css'
})
export class UtlaggComponent {

  constructor(private utlaggService: UtlaggService) {}

  get getUtlagg() {
    return this.utlaggService.getUtlagg();
  }
}
