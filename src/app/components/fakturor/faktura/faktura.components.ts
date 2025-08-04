import {Component, inject, Input} from '@angular/core';
import {Faktura} from '../../../model/faktura';
import {CardComponent} from '../../common/card/card.component';
import {FakturaService} from '../../../service/faktura.service';

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
  fakturaService = inject(FakturaService);

  onDeleteFaktura() {
    this.fakturaService.deleteFakturor(this.faktura.id);
  }

  onEditFaktura() {
   this.fakturaService.editFaktura(this.faktura);
  }

  onPaidFaktura() {
    console.log(this.faktura);
  }
}
