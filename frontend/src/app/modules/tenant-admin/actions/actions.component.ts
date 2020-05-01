import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { ActionListItemDto } from '@models/action';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ClientType } from '@models/report-case';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router) { }

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
      lastName: action.lastName || '-',
      firstName: action.firstName || '-',
      caseType: this.getTypeName(action.caseType),
      dateOfBirth: action.dateOfBirth ? action.dateOfBirth.toCustomLocaleDateString() : '-',
      email: action.email,
      phone: action.phone || '-',
      quarantineStart: action.quarantineStart ? action.quarantineStart.toCustomLocaleDateString() : '-',
      status: action.status,
      alerts: action.alerts || [],
      caseId: action.caseId
    };
  }

  private getTypeName(clientType: ClientType): string {
    switch (clientType) {
      case ClientType.Contact:
        return 'Kontakt';
      case ClientType.Index:
        return 'Index';
      default:
        return '-';
    }
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  sendMail(event, to: string) {
    event.stopPropagation();
    window.location.href = `mailto:${to}`;
  }

  onSelect(event) {
    console.log(event);
    this.router.navigate(
      ['/tenant-admin/client', event?.selected[0]?.caseId],
      {
        queryParams: { tab: 1 }
      });
  }
}
