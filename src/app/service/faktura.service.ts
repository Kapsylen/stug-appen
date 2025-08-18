import {afterNextRender, DestroyRef, inject, Injectable, signal} from '@angular/core';
import {Faktura, NewFaktura} from '../model/faktura';
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class FakturaService {

  private fakturor =  signal<Faktura[] | undefined>(undefined);
  private baseUrl = 'http://localhost:8080/api/v1/faktura';
  private destroyRef = inject(DestroyRef);

  constructor(private httpClient: HttpClient) {
    afterNextRender(() => {
        try {
          this.fetchFakturor()
        } catch (e) {
          console.error('Failed to parse fakturor data from localStorage:', e);
        }
    });
  }

  fetchFakturor() {
    const subscription = this.httpClient
      .get<Faktura[]>(this.baseUrl, {
        observe: 'body',
        responseType: 'json'
      })
    .subscribe({
        next: (fakturorData) => {
          this.fakturor.set(fakturorData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getFakturor() {
    return this.fakturor
  }

  deleteFakturor(id: string) {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    this.fakturor.update(fakturorList => fakturorList?.filter(f => f.id !== id));
    console.log('Fakturor deleted successfully');
  }

  saveFaktura(newFakture: NewFaktura) {
    console.log(newFakture);
    this.httpClient.post<Faktura>(this.baseUrl, newFakture)
      .subscribe({
        next: (newFakturorData) => {
          console.log({...newFakturorData});
          this.fakturor.update(fakturorList => [
            ...(fakturorList ?? []),
            newFakturorData
          ]);
        },
        error: (error) => {
          console.error('Failed to save fakturor:', error);
          // Handle error appropriately
        }
      })
  }

  editFaktura(faktura: Faktura) {
    this.httpClient.put<Faktura>(this.baseUrl + '/' + faktura.id, faktura)
      .subscribe({
        next: (fakturaData) => {
          console.log('Fakturor updated: ' + fakturaData);
        // TBD
        //  this.fakturor = this.fakturor.map(f => f.id === fakturaData.id ? fakturaData : f);
        }
      });
  }
}
