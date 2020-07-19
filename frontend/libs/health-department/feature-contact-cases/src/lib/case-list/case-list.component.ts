import { DateFunctions } from '@qro/shared/util-date';
import { SubSink } from 'subsink';
import { CaseDto, IndexCaseEntityService } from '@qro/health-department/domain';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { MatSort } from '@angular/material/sort';
import { BehaviorSubject, Observable } from 'rxjs';
import { DatatableComponent, SelectionType } from '@swimlane/ngx-datatable';
import { ClientType } from '@qro/auth/api';
import { map, tap } from 'rxjs/operators';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: ClientType;
  typeName: string;
  dateOfBirth: Date;
  createdAt: string;
  quarantineEnd: Date;
  caseId: string;
  status: string;
  extReferenceNumber: string;
  originCases: string;
}

@Component({
  selector: 'qro-contact-cases-case-list',
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.scss'],
})
export class CaseListComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  cases$: Observable<CaseRowViewModel[]>;
  loading = false;
  selectionType = SelectionType.single;
  @ViewChild(DatatableComponent) table: DatatableComponent;

  get filter(): string {
    return this._filter.value;
  }

  set filter(filter: string) {
    this._filter.next(filter);
  }

  private readonly _filter = new BehaviorSubject<string>('');
  private _filteredData = new BehaviorSubject<CaseRowViewModel[]>([]);

  get filteredData(): CaseRowViewModel[] {
    return this._filteredData.value;
  }

  set filteredData(filteredData: CaseRowViewModel[]) {
    this._filteredData.next(filteredData);
  }

  private dateTimeNow = new Date();

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private entityService: IndexCaseEntityService, private router: Router) {}

  ngOnInit(): void {
    this.loading = true;

    this.cases$ = this.entityService.entities$.pipe(
      map((dtos) => dtos.map((dto) => this.getRowData(dto))),
      tap((cases) => (this.filteredData = [...cases])),
      tap((cases) => (this.loading = false))
    );
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  public isQuarantineOngoing(endDate: Date | null): boolean {
    if (!endDate) {
      return null;
    }
    return endDate < this.dateTimeNow;
  }

  getRowData(c: CaseDto): CaseRowViewModel {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      typeName: c.caseTypeLabel,
      dateOfBirth: c.dateOfBirth,
      createdAt: c.createdAt ? DateFunctions.toCustomLocaleDateString(c.createdAt) : '-',
      quarantineEnd: c.quarantineEndDate,
      status: c.status,
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-',
      originCases: c.originCases.map((originCase) => `${originCase.firstName} ${originCase.lastName}`).join(', '),
    };
  }

  getQuarantineEndString(quarantineEnd: Date): string {
    if (!quarantineEnd) {
      return '-';
    }

    if (DateFunctions.isDateInPast(quarantineEnd)) {
      return 'beendet';
    }

    return DateFunctions.toCustomLocaleDateString(quarantineEnd);
  }

  updateFilter(event, cases: CaseRowViewModel[]) {
    this.filter = event.target.value;
    this.filteredData = !this.filter ? cases : cases.filter((obj) => this.filterPredicate(obj, this.filter));
    this.table.offset = 0;
  }

  filterPredicate(obj: CaseRowViewModel, filter: string): boolean {
    const dataStr = Object.keys(obj)
      .reduce((currentTerm: string, key: string) => {
        if (key === 'caseId') {
          return currentTerm;
        }
        if (obj[key] instanceof Date) {
          return (
            currentTerm + DateFunctions.toCustomLocaleDateString((obj as { [key: string]: any })[key] as Date) + '◬'
          );
        }
        return currentTerm + (obj as { [key: string]: any })[key] + '◬';
      }, '')
      .toLowerCase();
    const transformedFilter = filter.trim().toLowerCase();

    return dataStr.indexOf(transformedFilter) !== -1;
  }

  sendMail(event, to: string) {
    event.stopPropagation();
    window.open(`mailto:${to}`);
  }

  onSelect(event) {
    this.router.navigate(['/health-department/case-detail', event?.selected[0]?.type, event?.selected[0]?.caseId]);
  }
}
