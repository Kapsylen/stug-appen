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
      const utlaggData = localStorage.getItem('utlagg');
      if (utlaggData !== null) {
        try {
          const parsedData = JSON.parse(utlaggData);
          this.utlagg.set(parsedData);
        } catch (e) {
          console.error('Failed to parse utlagg data from localStorage:', e);
          this.fetchUtlagg();
        }

      } else {
        this.fetchUtlagg();
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
          localStorage.setItem('utlagg', JSON.stringify(utlaggData));
          console.log('Utlagg fetched: ' + utlaggData);
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
    localStorage.setItem('utlagg', JSON.stringify(this.utlagg()));
    console.log('Utlagg deleted successfully');
  }

  saveUtlagg(newUtlagg: NewUtlagg) {
    console.log(newUtlagg);
    this.httpClient.post<Utlagg>(this.baseUrl, newUtlagg)
      .subscribe({
        next: (newUtlaggData) => {
          console.log({...newUtlaggData});
          this.utlagg.update(utlaggList => [
            ...(utlaggList ?? []),
            newUtlaggData
          ]);
          const currentUtlagg = this.utlagg();
          if (currentUtlagg) {
            localStorage.setItem('utlagg', JSON.stringify(currentUtlagg));
          }
          console.log('Utlagg saved:', newUtlaggData);

        },
        error: (error) => {
          console.error('Failed to save utlagg:', error);
          // Handle error appropriately
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
