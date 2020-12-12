import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubSink } from 'subsink';
import { HealthDepartmentService } from '@qro/health-department/domain';
import { take, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { EventNewDialogComponent } from '../event-new-dialog/event-new-dialog.component';

export interface Event {
  title: string;
  start: string;
  end: string;
}

@Component({
  selector: 'qro-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss'],
})
export class EventListComponent implements OnInit, OnDestroy {
  subs = new SubSink();
  caseId = null;

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
  }

  ngOnInit(): void {}

  openNewEventDialog() {
    this.subs.add(
      this.dialog
        .open(EventNewDialogComponent)
        .afterClosed()
        .subscribe((value) => this.saveNewEvent(value))
    );
  }

  private saveNewEvent(value) {
    debugger;
    //todo richtig typisieren
    const newEvent: Event = {
      title: value.name,
      start: value.dateFrom,
      end: value.dateTo,
    };
    this.healthDepartmentService.addEvent(this.caseId, newEvent).subscribe((value1) => console.log(value1));
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
