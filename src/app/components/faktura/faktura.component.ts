import {Component, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Faktura} from '../model/faktura';

@Component({
  selector: 'app-faktura',
  imports: [
    CardComponent
  ],
  templateUrl: './faktura.component.html',
  styleUrl: './faktura.component.css'
})
export class FakturaComponent {
  @Input({required: true}) faktura!: Faktura;
}
