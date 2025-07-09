import {afterNextRender, DestroyRef, inject, Injectable} from '@angular/core';
import {NewUtlagg, Utlagg} from '../model/utlagg';
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class UtlaggService {

  private utlagg: Utlagg[] = [];
  private baseUrl = 'http://localhost:8080/api/v1/utlagg';
  private destroyRef = inject(DestroyRef);

  constructor(private httpClient: HttpClient) {
    afterNextRender(() => {
      const utlagg = localStorage.getItem('utlagg');
      if (utlagg) {
        this.utlagg = JSON.parse(utlagg);
      } else {
        this.fetchUtlagg();
      }
    });
  }

  fetchUtlagg() {
    const subscription = this.httpClient
      .get<Utlagg[]>('http://localhost:8080/api/v1/utlagg')
      .subscribe({
        next: (utlaggData) => {
          utlaggData.forEach(u => {
            this.utlagg.unshift(u);
            localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
          });
        },
        error: (error) => {
          console.log(error);
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
    this.utlagg = this.utlagg.filter(utlagg => utlagg.id !== id);
    localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
    console.log('Utlagg deleted: ' + this.utlagg);
  }

  saveUtlagg(newUtlagg: NewUtlagg) {
    this.httpClient.post<Utlagg>(this.baseUrl, newUtlagg)
      .subscribe({
        next: (newUtlaggData) => {
          console.log({...newUtlaggData});
          this.utlagg.unshift(newUtlaggData)
          localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
          console.log('Utlagg saved: ' + this.utlagg);
        }
      });
  }

  editUtlagg(utlagg: Utlagg) {
    this.httpClient.put<Utlagg>(this.baseUrl + '/' + utlagg.id, utlagg)
      .subscribe({
        next: (utlaggData) => {
          console.log('Utlagg updated: ' + utlaggData);
          this.utlagg = this.utlagg.map(u => u.id === utlaggData.id ? utlaggData : u);
        }
      });
  }
}
