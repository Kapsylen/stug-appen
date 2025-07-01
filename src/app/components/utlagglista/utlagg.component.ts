import {Component} from '@angular/core';
import {UtlaggComponents} from './utlagg/utlagg.components';
import {DataService} from '../../service/data.service';

@Component({
  selector: 'app-utlagglista',
  imports: [UtlaggComponents],
  templateUrl: './utlagg.component.html',
  styleUrl: './utlagg.component.css'
})
export class UtlaggComponent {

  constructor(private dataService: DataService) {}

  get getUtlagg() {
    return this.dataService.getUtlagg();
  }
}
