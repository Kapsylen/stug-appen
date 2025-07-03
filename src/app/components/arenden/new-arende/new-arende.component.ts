import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ArendeService} from '../../../service/arende.service';

@Component({
  selector: 'app-new-arende',
  imports: [
    FormsModule
  ],
  templateUrl: './new-arende.component.html',
  styleUrl: './new-arende.component.css'
})
export class NewArendeComponent {

  @Output() close = new EventEmitter<void>();
  arendenService = inject(ArendeService);

  enteredName = '';
  enteredDescription = '';
  enteredType = ''
  enteredPriority = '';
  enteredStatus = '';
  enteredReportedBy = '';
  enteredAssignedTo = '';
  enteredLocation = '';
  enteredEstimatedCost = '';
  enteredActualCost = '';
  enteredStartTime = '';
  enteredResolvedTime = '';
  enteredResolution = '';
  enteredRequiresContractor = '';
  enteredContractorInfo = '';
  enteredUpdates = '';
  enteredTags = '';
  enteredCreatedAt = '';
  enteredUpdatedAt = '';
  enteredStatusTime = '';
  enteredMessage = '';
  enteredUpdatedBy = '';
  enteredIssueStatus = '';


  onSubmit() {
    this.arendenService.addNewArende({
      title: this.enteredName,
      description: this.enteredDescription,
      type: this.enteredType,
      priority: this.enteredPriority,
      status: this.enteredStatus,
      reportedBy: this.enteredReportedBy,
      assignedTo: this.enteredAssignedTo,
      location: this.enteredLocation,
      estimatedCost: this.enteredEstimatedCost,
      actualCost: this.enteredActualCost,
      startTime: this.enteredStartTime,
      resolvedTime: this.enteredResolvedTime,
      resolution: this.enteredResolution,
      requiresContractor: this.enteredRequiresContractor,
      contractorInfo: this.enteredContractorInfo,
      updates: [
        {
          timestamp: this.enteredStatusTime,
          message: this.enteredMessage,
          updatedBy: this.enteredUpdatedBy,
          status: this.enteredIssueStatus
        }
      ],
      tags: [
       this.enteredTags
      ],
      createdAt: this.enteredCreatedAt,
      updatedAt: this.enteredUpdatedAt
    });
    this.close.emit();
  }

  onCancel() {
    this.close.emit();
  }
}
