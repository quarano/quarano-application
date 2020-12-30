import { ApiService } from '@qro/shared/util-data-access';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit, Input } from '@angular/core';
import { CaseEntityService, ContactListItemDto } from '@qro/health-department/domain';
import { Observable, combineLatest } from 'rxjs';
import { map, switchMap, shareReplay } from 'rxjs/operators';
import { ColDef, ColumnApi, GridApi } from 'ag-grid-community';
import { CheckboxFilterComponent, DE_LOCALE, DATE_FILTER_PARAMS } from '@qro/shared/ui-ag-grid';
import { DateFunctions } from '@qro/shared/util-date';

interface RowViewModel {
  firstName: string;
  lastName: string;
  isHealthStaff: string;
  isSenior: string;
  hasPreExistingConditions: string;
  lastContact: string;
  remark: string;
  status: string;
  caseType: string;
  caseId: string;
}

@Component({
  selector: 'qro-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.scss'],
})
export class ContactListComponent implements OnInit {
  caseName$: Observable<string>;
  rows$: Observable<RowViewModel[]>;
  defaultColDef: ColDef = {
    editable: false,
    filter: 'agTextColumnFilter',
    sortable: true,
  };
  columnDefs: ColDef[] = [];
  locale = DE_LOCALE;
  frameworkComponents;

  constructor(
    private router: Router,
    private snackbarService: SnackbarService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private entityService: CaseEntityService
  ) {
    this.frameworkComponents = { checkboxFilter: CheckboxFilterComponent };
    this.columnDefs = [
      { headerName: 'Nachname', field: 'lastName', flex: 2 },
      { headerName: 'Vorname', field: 'firstName', flex: 2 },
      {
        headerName: 'Med.',
        field: 'isHealthStaff',
        width: 100,
        filter: 'checkboxFilter',
      },
      {
        headerName: 'Ü60',
        field: 'isSenior',
        width: 100,
        filter: 'checkboxFilter',
      },
      {
        headerName: 'Vorerkrank.',
        field: 'hasPreExistingConditions',
        width: 150,
        filter: 'checkboxFilter',
      },
      {
        headerName: 'Letzter Kontakt',
        field: 'lastContact',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      {
        headerName: 'Anmerkung.',
        field: 'remark',
        width: 150,
        filter: 'checkboxFilter',
      },
      { headerName: 'Status', field: 'status', flex: 3, filter: 'checkboxFilter' },
    ];
  }

  ngOnInit() {
    const caseDetail$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      map(([id, entityMap]) => {
        return entityMap[id];
      }),
      shareReplay(1)
    );

    this.caseName$ = caseDetail$.pipe(map((detail) => `${detail.firstName} ${detail.lastName}`));

    this.rows$ = caseDetail$.pipe(
      switchMap((detail) => {
        if (detail._links.hasOwnProperty('contacts')) {
          return this.apiService
            .getApiCall<any>(detail, 'contacts')
            .pipe(map((result) => result?._embedded?.contacts?.map((c) => this.getRowData(c))));
        }
      })
    );
  }

  onSelect(event: any) {
    const selectedItem = event.node.data as RowViewModel;
    if (selectedItem?.caseId && selectedItem?.caseType) {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigate(['/health-department/case-detail', selectedItem.caseType, selectedItem.caseId]);
    } else {
      this.snackbarService.message(`Zu ${selectedItem.firstName} ${selectedItem.lastName} liegt noch kein Fall vor.`);
    }
  }

  private getRowData(listItem: ContactListItemDto): RowViewModel {
    return {
      firstName: listItem.firstName || '-',
      lastName: listItem.lastName || '-',
      status: listItem.caseStatusLabel,
      isHealthStaff: this.getBooleanText(listItem.isHealthStaff),
      isSenior: this.getBooleanText(listItem.isSenior),
      remark: listItem.remark,
      hasPreExistingConditions: this.getBooleanText(listItem.hasPreExistingConditions),
      caseId: listItem.caseId,
      caseType: listItem.caseType,
      lastContact: this.getDateString(listItem.contactDates.length > 0 ? new Date(listItem.contactDates[0]) : null),
    };
  }

  private getDateString(date: Date): string {
    return date ? DateFunctions.toCustomLocaleDateString(date) : '-';
  }

  private getBooleanText(value: boolean): string {
    if (value) {
      return 'ja';
    }
    if (value === false) {
      return 'nein';
    }
    return '?';
  }

  onGridReady(event: { columnApi: ColumnApi }) {
    event.columnApi.applyColumnState({
      state: [
        {
          colId: 'lastName',
          sort: 'desc',
        },
      ],
      defaultState: { sort: null },
    });
  }
}
