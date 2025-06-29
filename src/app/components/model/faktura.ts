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
