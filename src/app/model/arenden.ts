export type Prioritet = 'low' | 'medium' | 'high' | 'critical';
export type Status = 'new' | 'investigating' | 'in_progress' | 'resolved' | 'closed';
export type Typ = 'maintenance' | 'damage' | 'utility' | 'security' | 'pest' | 'weather' | 'other';

export interface ArendeStatus {
  id: string;
  timestamp: string;
  message: string;
  updatedBy: string;
  status: string;
}

export interface Arende {
  id: string;
  title: string;
  description: string;
  type: string;
  priority: string;
  status: string;
  reportedBy: string;
  assignedTo: string;
  location: string;
  estimatedCost?: string;
  actualCost?: string;
  startTime: string;
  resolvedTime?: string;
  resolution?: string;
  requiresContractor: string;
  contractorInfo?: string;
  updates: ArendeStatus[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

export interface NewArendeStatus {
  timestamp: string;
  message: string;
  updatedBy: string;
  status: string;
}

export interface NewArende {
  title: string;
  description: string;
  type: string;
  priority: string;
  status: string;
  reportedBy: string;
  assignedTo: string;
  location: string;
  estimatedCost?: string;
  actualCost?: string;
  startTime: string;
  resolvedTime?: string;
  resolution?: string;
  requiresContractor: string;
  contractorInfo?: string;
  updates: NewArendeStatus[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}
