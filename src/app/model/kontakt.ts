export type Kontakt = {
  id: string;
  name: string;
  company: string;
  category: string;
  phone: string;
  email: string;
  address: string;
  notes: string;
  status: string;   // Active/Inactive status
}

export type NewKontakt = {
  name: string;
  company: string;
  category: string;
  phone: string;
  email: string;
  address: string;
  notes: string;
  status: string;   // Active/Inactive status
}
