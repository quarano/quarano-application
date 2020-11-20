import { DateFunctions } from '@qro/shared/util-date';
import { CaseDto, CaseEntityService } from '@qro/health-department/domain';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CaseType } from '@qro/auth/api';
import { map } from 'rxjs/operators';
import { ColDef } from 'ag-grid-community';
import { DE_LOCALE, UnorderedListComponent } from '@qro/shared/ui-ag-grid';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: CaseType;
  typeName: string;
  dateOfBirth: Date;
  createdAt: Date;
  quarantineEnd: Date;
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

  constructor(private entityService: CaseEntityService, private router: Router) {
    this.columnDefs = [
      { headerName: 'Status', field: 'status' },
      { headerName: 'Nachname', field: 'lastName' },
      { headerName: 'Vorname', field: 'firstName' },
      { headerName: 'Typ', field: 'typeName' },
      {
        headerName: 'Quarantäne bis',
        field: 'quarantineEnd',
        filter: 'agDateColumnFilter',
        valueFormatter: this.quarantineEndDateFormatter,
      },
      {
        headerName: 'Angelegt am',
        field: 'createdAt',
        filter: 'agDateColumnFilter',
        valueFormatter: this.quarantineEndDateFormatter,
      },
      { headerName: 'Vorgangsnr.', field: 'extReferenceNumber' },
      {
        headerName: 'Ursprungsfälle',
        field: 'originCases',
        cellRendererFramework: UnorderedListComponent,
        tooltipField: 'firstName',
      },
    ];
  }

  createdAtFormatter(params: { value: Date }) {
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
      typeName: c.caseTypeLabel,
      dateOfBirth: c.dateOfBirth,
      createdAt: c.createdAt,
      quarantineEnd: c.quarantineEndDate,
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

  onGridReady(event: any) {
    event.api.sizeColumnsToFit();
  }

  getRowHeight(params) {
    return params.data.rowHeight;
  }
}
