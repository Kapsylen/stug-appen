import {afterNextRender, Injectable } from '@angular/core';
import {NewUtlagg, Utlagg} from '../model/utlagg';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';

@Injectable({providedIn: 'root'})
export class UtlaggService {

  utlagg : Utlagg[] = [];

/*  private utlagg: Utlagg[] = [
    {
      id: 1,
      title: "Painting",
      description: "facade painting",
      date: "2025-06-01",
      price: "5000SEK"
    },
    {
      id: 2,
      title: "Plumbing",
      description: "bathroom renovation",
      date: "2025-06-15",
      price: "8500SEK"
    },
    {
      id: 3,
      title: "Electricity",
      description: "new kitchen wiring",
      date: "2025-06-20",
      price: "3200SEK"
    },
    {
      id: 4,
      title: "Gardening",
      description: "lawn maintenance",
      date: "2025-06-22",
      price: "1500SEK"
    },
    {
      id: 5,
      title: "Carpentry",
      description: "deck repair",
      date: "2025-06-25",
      price: "4200SEK"
    }
  ];*/

  constructor(private httpClient: HttpClient) {
    afterNextRender(async () => {
      const utlagg = localStorage.getItem('utlagg');
      if (utlagg) {
        this.utlagg = JSON.parse(utlagg);
      } else {
        this.utlagg = await this.getUtlagg();
        localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
      }
    });
  }

  async getUtlagg() : Promise<Utlagg[]> {
    const response = await firstValueFrom(
      this.httpClient.get<Utlagg[]>('http://localhost:8080/v1/utlagg', {
        observe: 'response'
      })
    );
    return response.body ?? [];

  }

  deleteUtlagg(id: string) {
    this.httpClient.delete('http://localhost:8080/v1/utlagg' + id);
    console.log(this.utlagg);
  }

  saveUtlagg(newUtlagg: NewUtlagg) {
    this.httpClient.post<Utlagg>('http://localhost:8080/v1/utlagg', newUtlagg)
      .subscribe({
        next : (newUtlaggData) => this.utlagg.unshift(newUtlaggData)
      });

    localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
  }
}
