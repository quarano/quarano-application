import { DateFunctions } from '@qro/shared/util-date';
import { SubSink } from 'subsink';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import {
  ClientType,
  ActionListItemDto,
  AlertConfiguration,
  getAlertConfigurations,
  Alert,
} from '@qro/health-department/domain';
import { MatSelect } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { MatOption } from '@angular/material/core';

export class ActionRowViewModel {
  lastName: string;
  firstName: string;
  type: ClientType;
  typeName: string;
  dateOfBirth: string;
  email: string;
  quarantineStart: string;
  status: string;
  alerts: string[];
  caseId: string;
  createdAt: string;
}

@Component({
  selector: 'qro-contact-case-action-list',
  templateUrl: './action-list.component.html',
  styleUrls: ['./action-list.component.scss'],
})
export class ActionListComponent implements OnInit, OnDestroy {
  actions: ActionListItemDto[] = [];
  private subs = new SubSink();
  loading = false;
  rows: ActionRowViewModel[] = [];
  filteredRows: ActionRowViewModel[] = [];
  alertConfigs: AlertConfiguration[] = [];
  @ViewChild(MatSelect) filterSelect: MatSelect;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.loading = true;

    this.subs.add(
      this.route.data.subscribe(
        (data) => {
          this.actions = data.actions;
          this.rows = this.actions.map((action) => this.getRowData(action));
          this.filteredRows = [...this.rows];
          this.loading = false;

          this.alertConfigs = this.actions
            .reduce((acc, next) => {
              next.alerts.forEach((alert) => {
                if (!acc.includes(alert)) {
                  acc.push(alert);
                }
              });

              return acc;
            }, [])
            .map((alert) => {
              return getAlertConfigurations().find((c) => c.alert === alert);
            })
            .sort((a, b) => a.order - b.order);
        },
        () => (this.loading = false)
      )
    );
  }

  get isFiltered(): boolean {
    if (this.filterSelect) {
      return (this.filterSelect.selected as MatOption[]).length > 0;
    }
    return false;
  }

  getRowData(action: ActionListItemDto): ActionRowViewModel {
    return {
      lastName: action.lastName || '-',
      firstName: action.firstName || '-',
      type: action.caseType,
      typeName: action.caseTypeLabel,
      dateOfBirth: action.dateOfBirth ? DateFunctions.toCustomLocaleDateString(action.dateOfBirth) : '-',
      email: action.email,
      quarantineStart: action.quarantineStart ? DateFunctions.toCustomLocaleDateString(action.quarantineStart) : '-',
      status: action.status,
      alerts: action.alerts || [],
      caseId: action.caseId,
      createdAt: action.createdAt ? DateFunctions.toCustomLocaleDateString(action.createdAt) : '-',
    };
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  sendMail(event, to: string) {
    event.stopPropagation();
    window.location.href = `mailto:${to}`;
  }

  onSelect(event) {
    this.router.navigate(['/health-department/case-detail', event?.selected[0]?.type, event?.selected[0]?.caseId], {
      queryParams: { tab: 1 },
    });
  }

  onAlertFilterChanged(selectedValues: string[]) {
    if (selectedValues.length === 0) {
      this.filteredRows = this.rows;
      return;
    }
    this.filteredRows = this.rows.filter((r) => r.alerts.filter((a) => selectedValues.includes(a)).length > 0);
  }

  resetFilter() {
    this.filterSelect.value = null;
    this.onAlertFilterChanged([]);
  }

  alertConfigurationFor(alert: Alert) {
    return getAlertConfigurations().find((c) => c.alert === alert);
  }
}
