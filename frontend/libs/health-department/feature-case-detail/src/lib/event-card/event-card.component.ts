import { Component, Input } from '@angular/core';
import { HealthDepartmentService } from '@qro/health-department/domain';

@Component({
  selector: 'qro-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
})
export class EventCardComponent {
  @Input()
  occasion: any;

  constructor(private healthDepartmentService: HealthDepartmentService) {}

  deleteOccasion() {
    this.healthDepartmentService.deleteOccasion(this.occasion).subscribe((value) => console.log(value));
  }
}
