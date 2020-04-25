import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { ActionListItemDto } from '@models/action';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.scss']
})
export class ActionsComponent implements OnInit, OnDestroy {
  actions: ActionListItemDto[] = [];
  private subs = new SubSink();
  loading = false;
  rows = [];

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.loading = true;
    this.subs.add(this.route.data.subscribe(
      data => {
        this.actions = data.actions;
        this.rows = this.actions.map(action => this.getRowData(action));
        this.loading = false;
      },
      error => this.loading = false));
  }

  getRowData(action: ActionListItemDto): any {
    return {
      lastName: action.lastName,
      firstName: action.firstName,
      type: action.type,
      dateOfBirth: action.dateOfBirth.toCustomLocaleDateString(),
      email: action.email,
      phone: action.phone,
      quarantineStart: action.quarantineStart.toCustomLocaleDateString(),
      status: 'Nachverfolgung_aktiv',
      alerts: action.alerts
    };
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  onSelect(event) {
    console.log(event);
    alert('Änderungsformular öffnen');
  }
}
