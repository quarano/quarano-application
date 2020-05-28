import { DateFunctions } from '@qro/shared/util';
import { SubSink } from 'subsink';
import { CaseListItemDto, ClientType } from '@qro/health-department/domain';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSort } from '@angular/material/sort';
import { BehaviorSubject } from 'rxjs';
import { DatatableComponent, SelectionType } from '@swimlane/ngx-datatable';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: ClientType;
  typeName: string;
  dateOfBirth: string;
  createdAt: string;
  quarantineEnd: string;
  caseId: string;
  status: string;
  extReferenceNumber: string;
}

@Component({
  selector: 'qro-contact-cases-case-list',
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.scss']
})
export class CaseListComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  cases: CaseListItemDto[] = [];
  loading = false;
  selectionType = SelectionType.single;
  rows: CaseRowViewModel[] = [];
  @ViewChild(DatatableComponent) table: DatatableComponent;

  get filter(): string {
    return this._filter.value;
  }

  set filter(filter: string) {
    this._filter.next(filter);
  }

  private readonly _filter = new BehaviorSubject<string>('');
  private _filteredData = new BehaviorSubject<CaseListItemDto[]>([]);
  get filteredData(): CaseListItemDto[] {
    return this._filteredData.value;
  }

  set filteredData(filteredData: CaseListItemDto[]) {
    this._filteredData.next(filteredData);
    this.rows = filteredData.map(c => this.getRowData(c));
  }

  private dateTimeNow = new Date();

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.subs.add(this.route.data.subscribe(
      data => {
        this.cases = data.cases;
        this.filteredData = [...data.cases];
        this.loading = false;
      },
      () => this.loading = false));
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

  getRowData(c: CaseListItemDto): CaseRowViewModel {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      typeName: c.caseTypeLabel,
      dateOfBirth: c.dateOfBirth ? DateFunctions.toCustomLocaleDateString(c.dateOfBirth) : '-',
      createdAt: c.createdAt ? DateFunctions.toCustomLocaleDateString(c.createdAt) : '-',
      quarantineEnd: this.getQuarantineEndString(c.quarantineEnd),
      status: c.status,
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-'
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

  updateFilter(event) {
    this.filter = event.target.value;

    this.filteredData =
      !this.filter ? this.cases : this.cases.filter(obj => this.filterPredicate(obj, this.filter));

    this.table.offset = 0;
  }

  filterPredicate(obj: CaseListItemDto, filter: string): boolean {
    const dataStr = Object.keys(obj).reduce((currentTerm: string, key: string) => {
      return currentTerm + (obj as { [key: string]: any })[key] + '◬';
    }, '').toLowerCase();

    const transformedFilter = filter.trim().toLowerCase();

    return dataStr.indexOf(transformedFilter) !== -1;
  }

  sendMail(event, to: string) {
    event.stopPropagation();
    window.open(`mailto:${to}`);
  }

  onSelect(event) {
    this.router.navigate(['/tenant-admin/client', event?.selected[0]?.type, event?.selected[0]?.caseId]);
  }
}
