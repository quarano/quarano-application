import { map } from 'rxjs/operators';
import { CaseEntityService, CaseDto } from '@qro/health-department/domain';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { DateFunctions } from '@qro/shared/util-date';
import { CaseType } from '@qro/auth/api';
import { EmailButtonComponent, DE_LOCALE } from '@qro/shared/ui-ag-grid';
import { ColDef } from 'ag-grid-community';

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
  @ViewChild(DatatableComponent) table: DatatableComponent;
  defaultColDef: ColDef = {
    width: 150,
    editable: false,
    filter: 'agTextColumnFilter',
    sortable: true,
  };
  columnDefs: ColDef[] = [];
  locale = DE_LOCALE;

  constructor(private entityService: CaseEntityService, private router: Router) {
    this.columnDefs = [
      { headerName: 'Status', field: 'status', width: 270 },
      { headerName: 'Nachname', field: 'lastName' },
      { headerName: 'Vorname', field: 'firstName' },
      {
        headerName: 'Geburtsdatum',
        field: 'dateOfBirth',
        filter: 'agDateColumnFilter',
        width: 160,
        valueFormatter: this.birthDateFormatter,
      },
      {
        headerName: 'Quarantäne bis',
        field: 'quarantineEnd',
        filter: 'agDateColumnFilter',
        width: 170,
        valueFormatter: this.quarantineEndDateFormatter,
      },
      { headerName: 'PLZ', field: 'zipCode', filter: 'agNumberColumnFilter', width: 115 },
      { headerName: 'Vorgangsnummer', field: 'extReferenceNumber', width: 200 },
      {
        headerName: 'E-Mail',
        field: 'email',
        width: 120,
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
}
