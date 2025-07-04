import {afterNextRender, Injectable} from '@angular/core';
import {NewUtlagg, Utlagg} from '../model/utlagg';

@Injectable({providedIn: 'root'})
export class UtlaggService {

  private utlagg: Utlagg[] = [
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
  ];

  constructor() {
    afterNextRender(() => {
      const utlagg = localStorage.getItem('utlagg');
      if (utlagg) {
        this.utlagg = JSON.parse(utlagg);
      } else {
        localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
      }
    });
  }

  getUtlagg() {
    return this.utlagg;
  }

  deleteUtlagg(id: number) {
    this.utlagg = this.utlagg.filter(utlagg => utlagg.id !== id);
    console.log(this.utlagg);
    this.saveUtlagg()
  }

  saveUtlagg() {
    localStorage.setItem('utlagg', JSON.stringify(this.utlagg));
  }

  addUtlagg(newUtlagg: NewUtlagg) {
    this.utlagg.unshift({
      id: new Date().getTime().valueOf(),
      title: newUtlagg.title,
      description: newUtlagg.description,
      date: newUtlagg.date,
      price: newUtlagg.price
    });
    this.saveUtlagg();
  }
}
