import { map } from 'rxjs/operators';
import { CaseEntityService, CaseDto } from '@qro/health-department/domain';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { DateFunctions } from '@qro/shared/util-date';
import { CaseType } from '@qro/auth/api';
import { EmailButtonComponent, DE_LOCALE } from '@qro/shared/ui-ag-grid';
import { ColDef, GridApi } from 'ag-grid-community';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: CaseType;
  dateOfBirth: Date;
  email: string;
  quarantineEnd: Date;
  caseId: string;
  status: string;
  zipCode: number;
  extReferenceNumber: string;
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

  constructor(private entityService: CaseEntityService, private router: Router) {
    this.columnDefs = [
      { headerName: 'Status', field: 'status' },
      { headerName: 'Nachname', field: 'lastName' },
      { headerName: 'Vorname', field: 'firstName' },
      {
        headerName: 'Geburtsdatum',
        field: 'dateOfBirth',
        filter: 'agDateColumnFilter',
        valueFormatter: this.birthDateFormatter,
      },
      {
        headerName: 'Quarantäne bis',
        field: 'quarantineEnd',
        filter: 'agDateColumnFilter',
        valueFormatter: this.quarantineEndDateFormatter,
      },
      { headerName: 'PLZ', field: 'zipCode', filter: 'agNumberColumnFilter' },
      { headerName: 'Vorgangsnr.', field: 'extReferenceNumber' },
      {
        headerName: 'E-Mail',
        field: 'email',
        cellRendererFramework: EmailButtonComponent,
        filter: false,
        sortable: false,
      },
    ];
  }

  birthDateFormatter(params: { value: Date }) {
    return params.value ? DateFunctions.toCustomLocaleDateString(params.value) : '-';
  }

  quarantineEndDateFormatter(params: { value: Date }) {
    if (!params.value) {
      return '-';
    }

    if (DateFunctions.isDateInPast(params.value)) {
      return 'beendet';
    }

    return DateFunctions.toCustomLocaleDateString(params.value);
  }

  ngOnInit(): void {
    this.cases$ = this.entityService.filteredEntities$.pipe(map((dtos) => dtos.map((dto) => this.getRowData(dto))));
  }

  getRowData(c: CaseDto): CaseRowViewModel {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      dateOfBirth: c.dateOfBirth,
      email: c.email,
      quarantineEnd: c.quarantineEndDate,
      status: c.status,
      zipCode: Number.parseInt(c.zipCode, 10) || null,
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-',
    };
  }

  onSelect(event: any) {
    if (event.node.isSelected()) {
      this.router.navigate(['/health-department/case-detail', event.node.data.type, event.node.data.caseId]);
    }
  }

  onGridReady(event: { api: GridApi }) {
    event.api.sizeColumnsToFit();
  }
}
