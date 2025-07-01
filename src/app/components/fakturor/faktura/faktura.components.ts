import {Component, Input} from '@angular/core';
import {Faktura} from '../../model/faktura';
import {CardComponent} from '../../../common/card/card.component';

@Component({
  selector: 'app-faktura',
  imports: [
    CardComponent
  ],
  templateUrl: './faktura.components.html',
  styleUrl: './faktura.components.css'
})
export class FakturaComponents {
  @Input({required: true}) faktura!: Faktura;
}
