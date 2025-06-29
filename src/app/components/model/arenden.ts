export type Prioritet = 'low' | 'medium' | 'high' | 'critical';
export type Status = 'new' | 'investigating' | 'in_progress' | 'resolved' | 'closed';
export type Typ = 'maintenance' | 'damage' | 'utility' | 'security' | 'pest' | 'weather' | 'other';

export interface ArendeStatus {
  id: number;
  timestamp: string;
  message: string;
  updatedBy: string;
  status: Status;
}

export interface Arende {
  id: number;
  title: string;
  description: string;
  type: Typ;
  priority: Prioritet;
  status: Status;
  reportedBy: string;
  assignedTo: string;
  location: string;
  estimatedCost?: string;
  actualCost?: string;
  startTime: string;
  resolvedTime?: string;
  resolution?: string;
  requiresContractor: boolean;
  contractorInfo?: string;
  updates: ArendeStatus[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}
