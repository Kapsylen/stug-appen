import {afterNextRender, DestroyRef, inject, Injectable, signal} from '@angular/core';
import {NewUtlagg, Utlagg} from '../model/utlagg';
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class UtlaggService {

  private utlagg =  signal<Utlagg[] | undefined>(undefined);
  private baseUrl = 'http://localhost:8080/api/v1/utlagg';
  private destroyRef = inject(DestroyRef);

  constructor(private httpClient: HttpClient) {
    afterNextRender(() => {
        try {
          this.fetchUtlagg();
        } catch (e) {
          console.error('Failed to parse utlagg data from localStorage:', e);
        }
    });
  }

  fetchUtlagg() {
    const subscription = this.httpClient
      .get<Utlagg[]>(this.baseUrl, {
        observe: 'body',
        responseType: 'json'
      })
      .subscribe({
        next: (utlaggData) => {
          this.utlagg.set(utlaggData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getUtlagg() {
    return this.utlagg;
  }

  deleteUtlagg(id: string) {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    this.utlagg.update(signalUtlagg => signalUtlagg?.filter(u => u.id !== id));
    console.log('Utlagg deleted successfully');
  }

  saveUtlagg(newUtlagg: NewUtlagg) {
    this.httpClient.post<Utlagg>(this.baseUrl, newUtlagg)
      .subscribe({
        next: (newUtlaggData) => {
          console.log({...newUtlaggData});
          this.utlagg.update(utlaggList => [
            ...(utlaggList ?? []),
            newUtlaggData
          ]);
        },
        error: (error) => {
          console.error('Failed to save utlagg:', error);
        }
      });
  }

  editUtlagg(utlagg: Utlagg) {
    this.httpClient.put<Utlagg>(this.baseUrl + '/' + utlagg.id, utlagg)
      .subscribe({
        next: (utlaggData) => {
          console.log('Utlagg updated: ' + utlaggData);
        // TBD
        //  this.utlagg = this.utlagg.map(u => u.id === utlaggData.id ? utlaggData : u);
        }
      });
  }
}
