import {afterNextRender, Injectable} from '@angular/core';
import {Faktura, NewFaktura} from '../components/model/faktura';
import {DUMMY_FAKTUROR} from '../../assets/fakturor_data';

@Injectable({providedIn: 'root'})
export class FakturaService {

  private fakturor: Faktura[] = [
    {
      id: 1,
      invoiceNumber: "STG-2025-001",
      clientName: "Summer Family Rentals",
      issueDate: "2025-06-01",
      dueDate: "2025-06-15",
      items: [
        {
          id: 1,
          description: "Cottage Rental - High Season (2 weeks)",
          quantity: "14",
          price: "1200",
          total: "16800"
        },
        {
          id: 2,
          description: "Final Cleaning Service",
          quantity: "1",
          price: "1500",
          total: "1500"
        }
      ],
      totalAmount: "18300",
      status: "paid"
    },
    {
      id: 2,
      invoiceNumber: "STG-2025-002",
      clientName: "Nordic Cottage Management",
      issueDate: "2025-04-15",
      dueDate: "2025-05-15",
      items: [
        {
          id: 1,
          description: "Roof Repair and Maintenance",
          quantity: "1",
          price: "25000",
          total: "25000"
        },
        {
          id: 2,
          description: "Gutter Cleaning",
          quantity: "1",
          price: "2800",
          total: "2800"
        }
      ],
      totalAmount: "27800",
      status: "sent"
    },
    {
      id: 3,
      invoiceNumber: "STG-2025-003",
      clientName: "Weekend Getaway Group",
      issueDate: "2025-05-20",
      dueDate: "2025-06-03",
      items: [
        {
          id: 1,
          description: "Cottage Rental - Weekend Package",
          quantity: "3",
          price: "1500",
          total: "4500"
        },
        {
          id: 2,
          description: "Firewood Supply",
          quantity: "2",
          price: "400",
          total: "800"
        }
      ],
      totalAmount: "5300",
      status: "draft"
    },
    {
      id: 4,
      invoiceNumber: "STG-2025-004",
      clientName: "Lakeside Properties AB",
      issueDate: "2025-03-01",
      dueDate: "2025-04-01",
      items: [
        {
          id: 1,
          description: "Annual Property Insurance",
          quantity: "1",
          price: "12000",
          total: "12000"
        }
      ],
      totalAmount: "12000",
      status: "overdue"
    },
    {
      id: 5,
      invoiceNumber: "STG-2025-005",
      clientName: "Green Garden Services",
      issueDate: "2025-05-01",
      dueDate: "2025-05-15",
      items: [
        {
          id: 1,
          description: "Spring Garden Preparation",
          quantity: "1",
          price: "4500",
          total: "4500"
        },
        {
          id: 2,
          description: "Tree Trimming Service",
          quantity: "4",
          price: "800",
          total: "3200"
        }
      ],
      totalAmount: "7700",
      status: "paid"
    }
  ];

  constructor() {
    afterNextRender(() => {
      const fakturor = localStorage.getItem('fakturor');
      if (fakturor) {
        this.fakturor = JSON.parse(fakturor);
      } else {
        localStorage.setItem('fakturor', JSON.stringify(this.fakturor));
      }
    });
  }

  getFakturor() {
    return this.fakturor;
  }

  deleteFakturor(id: number) {
    this.fakturor = this.fakturor.filter(utlagg => utlagg.id !== id);
    console.log(this.fakturor);
    this.saveFakturor()
  }

  saveFakturor() {
    localStorage.setItem('fakturor', JSON.stringify(this.fakturor));
  }

  addFaktura(newFakturor: NewFaktura) {
    this.fakturor.unshift({
      id: new Date().getTime().valueOf(),
      invoiceNumber: newFakturor.invoiceNumber,
      clientName: newFakturor.clientName,
      issueDate: newFakturor.issueDate,
      dueDate: newFakturor.dueDate,
      items: [{
        id: new Date().getTime().valueOf(),
        description: newFakturor.items[0].description,
        quantity: newFakturor.items[0].quantity,
        price: newFakturor.items[0].price,
        total: newFakturor.items[0].total
      }],
      totalAmount: newFakturor.totalAmount,
      status: newFakturor.status
    });
    this.saveFakturor();
  }
}
