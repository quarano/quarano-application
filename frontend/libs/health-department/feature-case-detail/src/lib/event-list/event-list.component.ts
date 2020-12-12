import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EventNewDialogComponent } from '../event-new-dialog/event-new-dialog.component';
import { SubSink } from 'subsink';

@Component({
  selector: 'qro-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss'],
})
export class EventListComponent implements OnInit, OnDestroy {
  subs = new SubSink();

  constructor(private dialog: MatDialog) {}

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
    console.log(value);
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
