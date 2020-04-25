import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { Subject, BehaviorSubject, combineLatest } from 'rxjs';
import { ReportCaseDto } from '@models/report-case';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  cases: ReportCaseDto[] = [];
  loading = false;
  rows = [];
  @ViewChild(DatatableComponent) table: DatatableComponent;
  get filter(): string { return this._filter.value; }
  set filter(filter: string) { this._filter.next(filter); }
  private readonly _filter = new BehaviorSubject<string>('');
  private _filteredData = new BehaviorSubject<ReportCaseDto[]>([]);
  get filteredData(): ReportCaseDto[] { return this._filteredData.value; }
  set filteredData(filteredData: ReportCaseDto[]) {
    this._filteredData.next(filteredData);
    this.rows = filteredData.map(c => this.getRowData(c));
  }

  private dateTimeNow = new Date();

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.subs.add(this.route.data.subscribe(
      data => {
        this.cases = data.cases;
        this.filteredData = [...data.cases];
        this.loading = false;
      },
      error => this.loading = false));
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
      lastName: c.lastName,
      firstName: c.firstName,
      type: c.caseType,
      dateOfBirth: c.dateOfBirth.toCustomLocaleDateString(),
      email: c.email,
      phone: c.phone,
      quarantineEnd: c.quarantineEnd?.toCustomLocaleDateString(),
      status: c.status,
      zipCode: c.zipCode
    };
  }

  updateFilter(event) {
    this.filter = event.target.value;

    this.filteredData =
      !this.filter ? this.cases : this.cases.filter(obj => this.filterPredicate(obj, this.filter));

    this.table.offset = 0;
  }

  filterPredicate: ((obj: ReportCaseDto, filter: string) => boolean) = (obj: ReportCaseDto, filter: string): boolean => {
    const dataStr = Object.keys(obj).reduce((currentTerm: string, key: string) => {
      return currentTerm + (obj as { [key: string]: any })[key] + '◬';
    }, '').toLowerCase();

    const transformedFilter = filter.trim().toLowerCase();

    return dataStr.indexOf(transformedFilter) !== -1;
  }

  onSelect(event) {
    console.log(event);
    alert('Änderungsformular öffnen');
  }
}
