import { DATE_FILTER_PARAMS } from './../../../../../shared/ui-ag-grid/src/lib/date-filter-params';
import { MatInput } from '@angular/material/input';
import { DateFunctions } from '@qro/shared/util-date';
import { CaseDto, CaseEntityService } from '@qro/health-department/domain';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CaseType } from '@qro/auth/api';
import { map } from 'rxjs/operators';
import { ColDef, ColumnApi, GridApi } from 'ag-grid-community';
import {
  CheckboxFilterComponent,
  DE_LOCALE,
  EmailButtonComponent,
  UnorderedListComponent,
} from '@qro/shared/ui-ag-grid';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: CaseType;
  typeName: string;
  dateOfBirth: string;
  createdAt: string;
  quarantineEnd: string;
  caseId: string;
  status: string;
  extReferenceNumber: string;
  originCases: string[];
  rowHeight: number;
}

@Component({
  selector: 'qro-contact-cases-case-list',
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.scss'],
})
export class CaseListComponent implements OnInit {
  filterString = '';
  cases$: Observable<CaseRowViewModel[]>;
  defaultColDef: ColDef = {
    editable: false,
    filter: 'agTextColumnFilter',
    sortable: true,
  };
  columnDefs: ColDef[] = [];
  locale = DE_LOCALE;
  frameworkComponents;
  gridApi: GridApi;

  constructor(private entityService: CaseEntityService, private router: Router) {
    this.frameworkComponents = { checkboxFilter: CheckboxFilterComponent };
    this.columnDefs = [
      { headerName: 'Status', field: 'status', flex: 3, filter: 'checkboxFilter' },
      { headerName: 'Nachname', field: 'lastName', flex: 2 },
      { headerName: 'Vorname', field: 'firstName', flex: 2 },
      { headerName: 'Typ', field: 'typeName', filter: 'checkboxFilter' },
      {
        headerName: 'Quarantäne bis',
        field: 'quarantineEnd',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      {
        headerName: 'Angelegt am',
        field: 'createdAt',
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
      {
        headerName: 'E-Mail',
        field: 'email',
        cellRendererFramework: EmailButtonComponent,
        filter: false,
        width: 90,
      },
    ];
  }

  dateFormatter(value: Date): string {
    return value ? DateFunctions.toCustomLocaleDateString(value) : '-';
  }

  quarantineEndDateFormatter(value: Date): string {
    if (!value) {
      return '-';
    }

    if (DateFunctions.isDateInPast(value)) {
      return 'beendet';
    }

    return DateFunctions.toCustomLocaleDateString(value);
  }

  ngOnInit(): void {
    this.cases$ = this.entityService.filteredEntities$.pipe(map((dtos) => dtos.map((dto) => this.getRowData(dto))));
  }

  getRowData(c: CaseDto): CaseRowViewModel {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      typeName: c.caseTypeLabel,
      dateOfBirth: this.dateFormatter(c.dateOfBirth),
      createdAt: this.dateFormatter(c.createdAt),
      quarantineEnd: this.quarantineEndDateFormatter(c.quarantineEndDate),
      status: c.status,
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-',
      originCases: c?._embedded?.originCases?.map((originCase) => `${originCase?.firstName} ${originCase?.lastName}`),
      rowHeight: Math.min(50 + c?._embedded?.originCases?.length * 9),
    };
  }

  onSelect(event: any) {
    if (event.node.isSelected()) {
      this.router.navigate(['/health-department/case-detail', event.node.data.type, event.node.data.caseId]);
    }
  }

  getRowHeight(params) {
    return params.data.rowHeight;
  }

  onGridReady(event: { api: GridApi; columnApi: ColumnApi }) {
    this.gridApi = event.api;
    this.gridApi.setFilterModel({
      status: [
        {
          selected: false,
          label: 'abgeschlossen',
        },
      ],
    });
    this.gridApi.onFilterChanged();
    event.columnApi.applyColumnState({
      state: [
        {
          colId: 'lastName',
          sort: 'asc',
        },
      ],
      defaultState: { sort: null },
    });
  }

  clearAllFilters() {
    this.gridApi.setFilterModel(null);
    this.filterString = null;
    this.gridApi.setQuickFilter(null);
  }
}
