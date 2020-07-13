import { CaseListItemDto } from '@qro/health-department/domain';
import { SubSink } from 'subsink';
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { SelectionType, DatatableComponent } from '@swimlane/ngx-datatable';
import { BehaviorSubject } from 'rxjs';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { DateFunctions } from '@qro/shared/util-date';
import { ClientType } from '@qro/auth/api';

class CaseRowViewModel {
  lastName: string;
  firstName: string;
  type: ClientType;
  dateOfBirth: Date;
  email: string;
  quarantineEnd: Date;
  caseId: string;
  status: string;
  zipCode: string;
  extReferenceNumber: string;
}

@Component({
  selector: 'qro-index-cases-case-list',
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.scss'],
})
export class CaseListComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  cases: CaseRowViewModel[] = [];
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

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.loading = true;
    this.subs.add(
      this.route.data.subscribe(
        (data) => {
          this.cases = data.cases.map((c) => this.getRowData(c));
          this.filteredData = [...this.cases];
          this.loading = false;
        },
        () => (this.loading = false)
      )
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

  getRowData(c: CaseListItemDto): CaseRowViewModel {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      dateOfBirth: c.dateOfBirth,
      email: c.email,
      quarantineEnd: c.quarantineEnd,
      status: c.status,
      zipCode: c.zipCode || '-',
      caseId: c.caseId,
      extReferenceNumber: c.extReferenceNumber || '-',
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
    this.filteredData = !this.filter ? this.cases : this.cases.filter((obj) => this.filterPredicate(obj, this.filter));
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
