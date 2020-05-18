import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { BehaviorSubject } from 'rxjs';
import { DatatableComponent, SelectionType } from '@swimlane/ngx-datatable';
import {ReportCaseDto} from '../../../models/report-case';

@Component({
  selector: 'qro-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  cases: ReportCaseDto[] = [];
  loading = false;
  selectionType = SelectionType.single;
  rows = [];
  @ViewChild(DatatableComponent) table: DatatableComponent;

  get filter(): string {
    return this._filter.value;
  }

  set filter(filter: string) {
    this._filter.next(filter);
  }

  private readonly _filter = new BehaviorSubject<string>('');
  private _filteredData = new BehaviorSubject<ReportCaseDto[]>([]);
  get filteredData(): ReportCaseDto[] {
    return this._filteredData.value;
  }

  set filteredData(filteredData: ReportCaseDto[]) {
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

  getRowData(c: ReportCaseDto): any {
    return {
      lastName: c.lastName || '-',
      firstName: c.firstName || '-',
      type: c.caseType,
      typeName: c.caseTypeLabel,
      dateOfBirth: c.dateOfBirth ? c.dateOfBirth.toCustomLocaleDateString() : '-',
      email: c.email,
      phone: c.phone || '-',
      quarantineEnd: this.getQuarantineEndString(c.quarantineEnd),
      status: c.status,
      zipCode: c.zipCode || '-',
      caseId: c.caseId
    };
  }

  getQuarantineEndString(quarantineEnd: Date): string {
    if (!quarantineEnd) {
      return '-';
    }

    if (quarantineEnd.isDateInPast()) {
      return 'beendet';
    }

    return quarantineEnd.toCustomLocaleDateString();

  }

  updateFilter(event) {
    this.filter = event.target.value;

    this.filteredData =
      !this.filter ? this.cases : this.cases.filter(obj => this.filterPredicate(obj, this.filter));

    this.table.offset = 0;
  }

  filterPredicate(obj: ReportCaseDto, filter: string): boolean {
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
