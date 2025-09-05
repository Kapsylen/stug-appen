import {Arende} from '../app/model/arenden';

export const DUMMY_ARENDEN: Arende[] = [
  {
    id: "1",
    title: "Water Leak in Bathroom",
    description: "Water leaking from pipe under sink, causing floor damage",
    type: "damage",
    priority: "high",
    status: "resolved",
    reportedBy: "Lars Andersson",
    assignedTo: "Plumber AB",
    location: "Main Bathroom",
    estimatedCost: "5000",
    actualCost: "4500",
    startTime: "2025-06-20T08:15:00",
    resolvedTime: "2025-06-21T14:30:00",
    resolution: "Replaced damaged pipe and repaired floor",
    requiresContractor: true,
    contractorInfo: "Plumber AB, Tel: 070-123-4567",
    updates: [
      {
        id: "1",
        timestamp: "2025-06-20T09:00:00",
        message: "Plumber contacted, arriving tomorrow morning",
        updatedBy: "Lars Andersson",
        status: "in_progress"
      },
      {
        id: "2",
        timestamp: "2025-06-21T14:30:00",
        message: "Repair completed, water pressure tested",
        updatedBy: "Plumber AB",
        status: "resolved"
      }
    ],
    tags: ["plumbing", "water-damage", "bathroom"],
    createdAt: "2025-06-20T08:15:00",
    updatedAt: "2025-06-21T14:30:00"
  },
  {
    id: "2",
    title: "Heating System Failure",
    description: "No heat output from radiators, temperature dropping",
    type: "utility",
    priority: "critical",
    status: "in_progress",
    reportedBy: "Maria Svensson",
    assignedTo: "Heating Expert SE",
    location: "Entire Cottage",
    estimatedCost: "8000",
    startTime: "2025-06-24T17:00:00",
    requiresContractor: true,
    contractorInfo: "Heating Expert SE, Tel: 070-987-6543",
    updates: [
      {
        id: "1",
        timestamp: "2025-06-24T17:30:00",
        message: "Emergency call placed to heating specialist",
        updatedBy: "Maria Svensson",
        status: "investigating"
      }
    ],
    tags: ["heating", "urgent", "winter"],
    createdAt: "2025-06-24T17:00:00",
    updatedAt: "2025-06-24T17:30:00"
  },
  {
    id: "3",
    title: "Wasp Nest in Attic",
    description: "Large wasp nest discovered in attic space",
    type: "pest",
    priority: "medium",
    status: "resolved",
    reportedBy: "Erik Nilsson",
    assignedTo: "Pest Control AB",
    location: "Attic",
    estimatedCost: "2500",
    actualCost: "2500",
    startTime: "2025-06-15T10:00:00",
    resolvedTime: "2025-06-16T15:00:00",
    resolution: "Nest safely removed and entry point sealed",
    requiresContractor: true,
    contractorInfo: "Pest Control AB, Tel: 070-555-1234",
    updates: [
      {
        id: "1",
        timestamp: "2025-06-15T11:00:00",
        message: "Pest control scheduled for tomorrow",
        updatedBy: "Erik Nilsson",
        status: "in_progress"
      },
      {
        id: "2",
        timestamp: "2025-06-16T15:00:00",
        message: "Nest removed and preventive measures applied",
        updatedBy: "Pest Control AB",
        status: "resolved"
      }
    ],
    tags: ["pest", "wasps", "attic"],
    createdAt: "2025-06-15T10:00:00",
    updatedAt: "2025-06-16T15:00:00"
  },
  {
    id: "4",
    title: "Storm Damage to Roof",
    description: "Several shingles blown off during storm, minor leaking",
    type: "weather",
    priority: "high",
    status: "new",
    reportedBy: "Anna Berg",
    assignedTo: "",
    location: "Roof - South Side",
    startTime: "2025-06-25T07:00:00",
    requiresContractor: true,
    updates: [],
    tags: ["roof", "storm-damage", "urgent"],
    createdAt: "2025-06-25T07:00:00",
    updatedAt: "2025-06-25T07:00:00"
  },
  {
    id: "5",
    title: "Broken Window Lock",
    description: "Kitchen window lock mechanism not functioning",
    type: "security",
    priority: "medium",
    status: "closed",
    reportedBy: "Johan Lindberg",
    assignedTo: "Handyman Services",
    location: "Kitchen Window",
    estimatedCost: "1200",
    actualCost: "1000",
    startTime: "2025-06-10T13:00:00",
    resolvedTime: "2025-06-10T15:30:00",
    resolution: "Replaced lock mechanism",
    requiresContractor: false,
    updates: [
      {
        id: "1",
        timestamp: "2025-06-10T15:30:00",
        message: "New lock installed and tested",
        updatedBy: "Handyman Services",
        status: "closed"
      }
    ],
    tags: ["security", "window", "repair"],
    createdAt: "2025-06-10T13:00:00",
    updatedAt: "2025-06-10T15:30:00"
  }
];
