export interface FakturaEnhet {
  id: number;
  description: string;
  quantity: string;
  price: string;
  total: string;
}

export interface Faktura {
  id: number;
  invoiceNumber: string;
  clientName: string;
  issueDate: string;
  dueDate: string;
  items: FakturaEnhet[];
  totalAmount: string;
  status: string;
}

export interface NewFakturaEnhet {
  description: string;
  quantity: string;
  price: string;
  total: string;
}

export interface NewFaktura {
  invoiceNumber: string;
  clientName: string;
  issueDate: string;
  dueDate: string;
  items: NewFakturaEnhet[];
  totalAmount: string;
  status: string;
}
