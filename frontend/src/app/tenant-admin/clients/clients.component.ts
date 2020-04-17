import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {ApiService} from '../../services/api.service';
import {takeUntil} from 'rxjs/operators';
import {Subject} from 'rxjs';
import {ReportCaseDto} from '../../models/report-case';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit, OnDestroy {
  public displayedColumns: string[] = ['lastName', 'firstName', 'zipCode', 'dateOfBirth', 'email',
    'type', 'medicalStaff', 'quarantineStart'];
  public dataSource = new MatTableDataSource<ReportCaseDto>();
  private dateTimeNow = new Date();

  private readonly destroy$$ = new Subject();

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(
    private apiService: ApiService) {
  }

  ngOnInit(): void {
    this.apiService.getCases()
      .pipe(
        takeUntil(this.destroy$$)
      )
      .subscribe(cases => {
        this.dataSource.data = cases;
        this.dataSource.sort = this.sort;
      });
  }

  ngOnDestroy(): void {
    this.destroy$$.next();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  public isQuarantineOngoing(endDate: Date | null): boolean {
    if (!endDate) {
      return null;
    }
    return endDate < this.dateTimeNow;
  }

}
