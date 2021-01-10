import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubSink } from 'subsink';
import { HealthDepartmentService } from '@qro/health-department/domain';
import { filter, switchMap, take, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { EventNewDialogComponent } from '../event-new-dialog/event-new-dialog.component';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'qro-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss'],
})
export class EventListComponent implements OnInit, OnDestroy {
  subs = new SubSink();
  caseId = null;

  $occasions: Observable<any[]>;

  constructor(
    private dialog: MatDialog,
    private healthDepartmentService: HealthDepartmentService,
    private route: ActivatedRoute
  ) {
    this.route.parent.paramMap
      .pipe(
        take(1),
        tap((params) => (this.caseId = params.get('id')))
      )
      .subscribe();

    this.$occasions = this.healthDepartmentService.getOccasion().pipe(
      tap((occasionsDTO) => console.log(occasionsDTO)),
      switchMap((occasions) => of(occasions?._embedded?.occasions))
    );
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
    this.healthDepartmentService
      .addOccasion(this.caseId, newOccasion)
      .subscribe((response) => console.log('backendResponse:', response));
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
