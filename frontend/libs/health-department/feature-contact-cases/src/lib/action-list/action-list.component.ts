import { DateFunctions } from '@qro/shared/util-date';
import { Component, OnInit } from '@angular/core';
import { ActionListItemDto, AlertConfiguration, getAlertConfigurations, Alert } from '@qro/health-department/domain';
import { ActivatedRoute, Router } from '@angular/router';
import { CaseType } from '@qro/auth/api';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ColDef, ColumnApi, GridApi } from 'ag-grid-community';
import { CheckboxFilterComponent, DE_LOCALE, UnorderedListComponent, DATE_FILTER_PARAMS } from '@qro/shared/ui-ag-grid';
import { ActionAlertComponent, ActionAlertFilterComponent } from '@qro/health-department/ui-action-alert';

export class ActionRowViewModel {
  lastName: string;
  firstName: string;
  type: CaseType;
  typeName: string;
  dateOfBirth: string;
  email: string;
  quarantineStart: string;
  status: string;
  alerts: string[];
  caseId: string;
  createdAt: string;
  originCases: string[];
  rowHeight: number;
}

@Component({
  selector: 'qro-contact-case-action-list',
  templateUrl: './action-list.component.html',
  styleUrls: ['./action-list.component.scss'],
})
export class ActionListComponent implements OnInit {
  actions$: Observable<ActionListItemDto[]>;
  rows$: Observable<ActionRowViewModel[]>;
  alertConfigs$: Observable<AlertConfiguration[]>;
  defaultColDef: ColDef = {
    editable: false,
    filter: 'agTextColumnFilter',
    sortable: false,
  };
  columnDefs: ColDef[] = [];
  locale = DE_LOCALE;
  frameworkComponents;

  constructor(private route: ActivatedRoute, private router: Router) {
    this.frameworkComponents = {
      checkboxFilter: CheckboxFilterComponent,
      actionAlertComponent: ActionAlertComponent,
      actionAlertFilter: ActionAlertFilterComponent,
    };
    this.columnDefs = [
      {
        headerName: 'Auffälligkeiten',
        field: 'alerts',
        flex: 4,
        filter: 'actionAlertFilter',
        cellRenderer: 'actionAlertComponent',
      },
      { headerName: 'Nachname', field: 'lastName', flex: 2 },
      { headerName: 'Vorname', field: 'firstName', flex: 2 },
      {
        headerName: 'Geburtsdatum',
        field: 'dateOfBirth',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      {
        headerName: 'Typ',
        field: 'typeName',
        filter: 'checkboxFilter',
        width: 110,
      },
      {
        headerName: 'Angelegt am',
        field: 'createdAt',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      { headerName: 'Status', field: 'status', flex: 3, filter: 'checkboxFilter' },
      {
        headerName: 'Quarantäne seit',
        field: 'quarantineStart',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      { headerName: 'Vorgangsnr.', field: 'extReferenceNumber', flex: 3 },
      {
        headerName: 'Ursprungsfälle',
        field: 'originCases',
        cellRendererFramework: UnorderedListComponent,
        flex: 2,
      },
    ];
  }

  dateFormatter(value: Date) {
    return value ? DateFunctions.toCustomLocaleDateString(value) : '-';
  }

  ngOnInit() {
    this.actions$ = this.route.data.pipe(map((data) => data.actions));

    this.rows$ = this.actions$.pipe(map((actions) => actions.map((action) => this.getRowData(action))));

    this.alertConfigs$ = this.actions$.pipe(
      map((actions) =>
        actions
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
          .sort((a, b) => a.order - b.order)
      )
    );
  }

  getRowData(action: ActionListItemDto): ActionRowViewModel {
    return {
      lastName: action.lastName || '-',
      firstName: action.firstName || '-',
      type: action.caseType,
      typeName: action.caseTypeLabel,
      dateOfBirth: this.dateFormatter(action.dateOfBirth),
      email: action.email,
      quarantineStart: this.dateFormatter(action.quarantineStart),
      status: action.status,
      alerts: action.alerts || [],
      caseId: action.caseId,
      createdAt: this.dateFormatter(action.createdAt),
      originCases: action.originCases.map((c) => `${c.firstName} ${c.lastName}`),
      rowHeight: Math.min(50 + Math.max(action.originCases.length, action.alerts.length) * 9),
    };
  }

  onGridReady(event: { api: GridApi; columnApi: ColumnApi }) {
    event.api.setFilterModel({
      status: [
        {
          selected: false,
          label: 'abgeschlossen',
        },
      ],
    });
    event.api.onFilterChanged();
  }

  onSelect(event: any) {
    this.router.navigate(['/health-department/case-detail', event.node.data.type, event.node.data.caseId, 'actions']);
  }

  alertConfigurationFor(alert: Alert) {
    return getAlertConfigurations().find((c) => c.alert === alert);
  }

  getRowHeight(params) {
    return params.data.rowHeight;
  }
}
