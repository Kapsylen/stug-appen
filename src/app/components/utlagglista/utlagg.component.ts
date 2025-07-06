import {Component} from '@angular/core';
import {UtlaggComponents} from './utlagg/utlagg.components';
import {UtlaggService} from '../../service/utlagg.service';
import {NewUtlaggComponent} from './new-utlagg/new-utlagg.component';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-utlagglista',
  imports: [UtlaggComponents, NewUtlaggComponent, AsyncPipe],
  templateUrl: './utlagg.component.html',
  styleUrl: './utlagg.component.css'
})
export class UtlaggComponent {

  isAddingUtlagg = false;

  constructor(private utlaggService: UtlaggService) {}

  get getUtlagg() {
    return this.utlaggService.getUtlagg();
  }

  onStartAddUtlagg() {
    this.isAddingUtlagg = true;
  }

  onCloseAddUtlagg() {
    this.isAddingUtlagg = false;
  }
}
