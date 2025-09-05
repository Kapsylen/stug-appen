import {Component, inject, Input} from '@angular/core';
import {NewUtlagg, Utlagg} from '../../../model/utlagg';
import {CardComponent} from '../../common/card/card.component';
import {UtlaggService} from '../../../service/utlagg.service';

@Component({
  selector: 'app-utlagg',
  imports: [
    CardComponent
  ],
  templateUrl: './utlagg.components.html',
  styleUrl: './utlagg.components.css'
})
export class UtlaggComponents {
  @Input({required: true}) utlagg!: Utlagg;
  private utlaggService = inject(UtlaggService)

  onEditUtlagg() {
    this.utlaggService.editUtlagg(this.utlagg);
  }

  onDeleteUtlagg() {
    this.utlaggService.deleteUtlagg(this.utlagg.id);
  }
}
