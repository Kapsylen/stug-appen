import {afterNextRender, Injectable} from '@angular/core';
import {Kontakt, NewKontakt} from '../components/model/kontakt';

@Injectable({providedIn: 'root'})
export class KontaktService {

  private kontakter: Kontakt[] = [
    {
      id: 1,
      name: "Erik Andersson",
      company: "Andersson's Electrical Solutions",
      category: "Electrician",
      phone: "+46-70-123-4567",
      email: "erik@anderssonelectrical.se",
      address: "Verkstadsgatan 12, 12345 Stockholm",
      notes: "Licensed Electrician, Solar Panel and EV Charging Installation certified. Rate: 650 SEK/h. Rating: 4.8/5",
      status: "active"
    },
    {
      id: 2,
      name: "Maria Johansson",
      company: "Modern Plumbing AB",
      category: "Plumber",
      phone: "+46-70-234-5678",
      email: "maria@modernplumbing.se",
      address: "Rörgatan 8, 12346 Stockholm",
      notes: "Master Plumber, Heat Pump Specialist. Green Plumbing Certified. Rate: 625 SEK/h. Rating: 4.9/5",
      status: "active"
    },
    {
      id: 3,
      name: "Lars Nilsson",
      company: "Nilsson Carpentry & Design",
      category: "Carpenter",
      phone: "+46-70-345-6789",
      email: "lars@nilssoncarpentry.se",
      address: "Trävägen 15, 12347 Stockholm",
      notes: "Master Carpenter, Kitchen Installation Specialist. Rate: 580 SEK/h. Rating: 4.7/5",
      status: "active"
    },
    {
      id: 4,
      name: "Anna Lindström",
      company: "Färg & Form Måleri",
      category: "Painter",
      phone: "+46-70-456-7890",
      email: "anna@fargochform.se",
      address: "Målarvägen 23, 12348 Stockholm",
      notes: "Professional Painter, Eco-friendly Paint Specialist. Rate: 495 SEK/h. Rating: 4.6/5",
      status: "active"
    },
    {
      id: 5,
      name: "Karl Bergström",
      company: "Bergström's HVAC",
      category: "HVAC",
      phone: "+46-70-567-8901",
      email: "karl@bergstromhvac.se",
      address: "Klimatgatan 45, 12349 Stockholm",
      notes: "HVAC Master Technician, Ventilation & Heat Pump Expert. Rate: 675 SEK/h. Rating: 4.9/5",
      status: "active"
    },
    {
      id: 6,
      name: "Gustav Holm",
      company: "Holm Masonry & Stone",
      category: "Mason",
      phone: "+46-70-678-9012",
      email: "gustav@holmmasonry.se",
      address: "Stengatan 67, 12350 Stockholm",
      notes: "Master Mason, Heritage Building Specialist. Rate: 590 SEK/h. Rating: 4.8/5",
      status: "active"
    },
    {
      id: 7,
      name: "Sofia Ekström",
      company: "Ekström Glass & Windows",
      category: "Glazier",
      phone: "+46-70-789-0123",
      email: "sofia@ekstromglass.se",
      address: "Fönstervägen 34, 12351 Stockholm",
      notes: "Certified Glazier, Energy Efficient Window Specialist. Rate: 560 SEK/h. Rating: 4.7/5",
      status: "active"
    },
    {
      id: 8,
      name: "Henrik Söderberg",
      company: "Söderberg Roofing",
      category: "Roofer",
      phone: "+46-70-890-1234",
      email: "henrik@soderbergroofing.se",
      address: "Takgatan 89, 12352 Stockholm",
      notes: "Master Roofer, Solar & Green Roof Specialist. Rate: 640 SEK/h. Rating: 4.8/5",
      status: "active"
    }
  ];


  constructor() {
    afterNextRender(() => {
      const kontakter = localStorage.getItem('kontakter');
      if (kontakter) {
        this.kontakter = JSON.parse(kontakter);
      } else {
        localStorage.setItem('kontakter', JSON.stringify(this.kontakter));
      }
    });
  }

  getKontakter() {
    return this.kontakter;
  }

  deleteKontakter(id: number) {
    this.kontakter = this.kontakter.filter(utlagg => utlagg.id !== id);
    console.log(this.kontakter);
    this.saveKontakter()
  }

  saveKontakter() {
    localStorage.setItem('kontakter', JSON.stringify(this.kontakter));
  }

  addKontak(kontak: NewKontakt) {
    this.kontakter.unshift({
      id: new Date().getTime().valueOf(),
      name: kontak.name,
      company: kontak.company,
      category: kontak.category,
      phone: kontak.phone,
      email: kontak.email,
      address: kontak.address,
      notes: kontak.notes,
      status: kontak.status
    });
    this.saveKontakter();
  }
}
