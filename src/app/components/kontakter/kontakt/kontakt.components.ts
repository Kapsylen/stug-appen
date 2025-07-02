import {Component, inject, Input} from '@angular/core';
import {CardComponent} from '../../common/card/card.component';
import {Kontakt} from '../../model/kontakt';
import {KontaktService} from '../../../service/kontakt.service';

@Component({
  selector: 'app-kontakt',
  imports: [
    CardComponent
  ],
  templateUrl: './kontakt.components.html',
  styleUrl: './kontakt.components.css'
})
export class KontaktComponents {
  @Input() kontakt!: Kontakt;
  kontakerService = inject(KontaktService);

  onDeleteKontakt() {
    this.kontakerService.deleteKontakter(this.kontakt.id);
  }

  onEditKontakt() {
    console.log(this.kontakt);
  }
}
