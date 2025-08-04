import {afterNextRender, DestroyRef, inject, Injectable, signal} from '@angular/core';
import {Kontakt, NewKontakt} from '../model/kontakt';
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class KontaktService {

  private kontakter = signal<Kontakt[] | undefined>(undefined);
  private baseUrl = 'http://localhost:8080/api/v1/kontakt';
  private destroyRef = inject(DestroyRef);

  constructor(private httpClient: HttpClient) {
    afterNextRender(() => {
      try {
        this.fetchKontakter();
      } catch (e) {
        console.error('Failed to parse kontakter data from backend api:', e);
      }
    });
  }

  fetchKontakter() {
    const subscription = this.httpClient
      .get<Kontakt[]>(this.baseUrl, {
        observe: 'body',
        responseType: 'json'
      })
      .subscribe({
        next: (kontakterData) => {
          this.kontakter.set(kontakterData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getKontakter() {
    return this.kontakter;
  }

  deleteKontakt(id: string) {
    this.kontakter.update(signalKontakt => signalKontakt?.filter(k => k.id !== id));
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    console.log('Kontakt deleted successfully');
  }

  saveKontakt(kontak: NewKontakt) {
    this.httpClient.post<Kontakt>(this.baseUrl, kontak)
      .subscribe({
        next: (newKontaktData) => {
          console.log({...newKontaktData});
          this.kontakter.update(kontakList => [
            ...(kontakList ?? []),
            newKontaktData
          ]);
        }
      })
  }
}
