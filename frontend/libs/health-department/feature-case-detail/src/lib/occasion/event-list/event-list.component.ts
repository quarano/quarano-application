import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubSink } from 'subsink';
import { HealthDepartmentService } from '@qro/health-department/domain';
import { filter, switchMap, take, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { EventNewDialogComponent } from '../event-new-dialog/event-new-dialog.component';
import { Observable, of } from 'rxjs';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';
import { OccasionService } from '../occasion.service';

@Component({
  selector: 'qro-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss'],
})
export class EventListComponent implements OnInit, OnDestroy {
  subs = new SubSink();
  caseId = null;

  $occasions: Observable<OccasionDto[]>;

  constructor(private dialog: MatDialog, private route: ActivatedRoute, private occasionService: OccasionService) {
    this.route.parent.paramMap
      .pipe(
        take(1),
        tap((params) => (this.caseId = params.get('id')))
      )
      .subscribe();

    this.$occasions = this.occasionService.getOccasions();
  }

  ngOnInit(): void {}

  openNewEventDialog() {
    this.subs.add(
      this.dialog
        .open(EventNewDialogComponent)
        .afterClosed()
        .pipe(filter((occasionData) => occasionData))
        .subscribe((occasionData) => this.saveNewEvent(occasionData))
    );
  }

  private saveNewEvent(newOccasion) {
    this.occasionService
      .saveOccasion(this.caseId, newOccasion)
      .pipe(take(1))
      .subscribe((value) => console.log(value));
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
