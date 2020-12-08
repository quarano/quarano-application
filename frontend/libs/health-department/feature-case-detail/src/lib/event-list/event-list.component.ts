import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EventNewDialogComponent } from '../event-new-dialog/event-new-dialog.component';

@Component({
  selector: 'qro-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss'],
})
export class EventListComponent implements OnInit {
  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {}

  openNewEventDialog() {
    this.dialog.open(EventNewDialogComponent);
  }
}
