import { BadRequestService } from '@qro/shared/ui-error';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { finalize, map, tap } from 'rxjs/operators';
import { CaseEntityService, CaseDto, HealthDepartmentService } from '@qro/health-department/domain';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { DateFunctions } from '@qro/shared/util-date';
import { CaseType } from '@qro/auth/api';
import { EmailButtonComponent, DE_LOCALE, CheckboxFilterComponent, DATE_FILTER_PARAMS } from '@qro/shared/ui-ag-grid';
import { ColDef, ColumnApi, GridApi } from 'ag-grid-community';
import { HttpResponse } from '@angular/common/http';
import * as fileSaver from 'file-saver';
import * as moment from 'moment';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: CaseType;
  dateOfBirth: string;
  email: string;
  quarantineEnd: string;
  caseId: string;
  status: string;
  zipCode: number;
  extReferenceNumber: string;
  selfLink: string;
}

@Component({
  selector: 'qro-index-cases-case-list',
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
  gridApi: GridApi;
  frameworkComponents;
  loading = false;

  constructor(
    private entityService: CaseEntityService,
    private router: Router,
    private healthDepartmentService: HealthDepartmentService,
    private snackbar: SnackbarService,
    private badRequestService: BadRequestService
  ) {
    this.frameworkComponents = { checkboxFilter: CheckboxFilterComponent };
    this.columnDefs = [
      { headerName: 'Status', field: 'status', flex: 3, filter: 'checkboxFilter' },
      { headerName: 'Nachname', field: 'lastName', flex: 2, tooltipField: 'lastName' },
      { headerName: 'Vorname', field: 'firstName', flex: 2 },
      {
        headerName: 'Geburtsdatum',
        field: 'dateOfBirth',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      {
        headerName: 'Quarantäne bis',
        field: 'quarantineEnd',
        filter: 'agDateColumnFilter',
        width: 170,
        filterParams: DATE_FILTER_PARAMS,
      },
      { headerName: 'PLZ', field: 'zipCode', filter: 'agNumberColumnFilter', width: 100 },
      { headerName: 'Vorgangsnr.', field: 'extReferenceNumber', flex: 3 },
      {
        headerName: 'E-Mail',
        field: 'email',
        cellRendererFramework: EmailButtonComponent,
        filter: false,
        sortable: false,
        width: 90,
      },
    ];
  }

  birthDateFormatter(params: { value: Date }) {
    return params.value ? DateFunctions.toCustomLocaleDateString(params.value) : '-';
  }

  getQuarantineEndDate(value: Date): string {
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
      dateOfBirth: c.dateOfBirth ? DateFunctions.toCustomLocaleDateString(c.dateOfBirth) : '-',
      email: c.email,
      quarantineEnd: this.getQuarantineEndDate(c.quarantineEndDate),
      status: c.status,
      zipCode: Number.parseInt(c.zipCode, 10) || null,
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-',
      selfLink: c._links?.self?.href,
    };
  }

  onSelect(event: any) {
    if (event.node.isSelected()) {
      this.router.navigate(['/health-department/case-detail', event.node.data.type, event.node.data.caseId]);
    }
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

  exportFilteredCases() {
    this.loading = true;
    // @ts-ignore
    const ids = this.gridApi.getModel().rowsToDisplay.map((r) => r.data.selfLink);
    this.healthDepartmentService
      .getCsvDataByIdList(ids)
      .pipe(
        map((result: HttpResponse<string>) => new Blob([result.body], { type: result.headers.get('Content-Type') })),
        tap((blob) => {
          fileSaver.saveAs(blob, `csvexport_index_gefiltert_${moment().format('YYYYMMDDHHmmss')}.csv`);
        }),
        finalize(() => (this.loading = false))
      )
      .subscribe(
        (_) => {
          this.snackbar.success('CSV-Export erfolgreich ausgeführt');
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, null);
        }
      );
  }

  get areCasesVisible(): boolean {
    if (!this.gridApi) {
      return false;
    }
    // @ts-ignore
    return this.gridApi.getModel().rowsToDisplay.length > 0;
  }
}
