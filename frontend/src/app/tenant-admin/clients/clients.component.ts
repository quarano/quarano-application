import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { BackendClient } from '../../models/backend-client';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { ApiService } from '../../services/api.service';
import { TenantClient } from '../../models/tenant-client';
import { DiaryEntryDto } from '../../models/diary-entry';
import { filter, takeUntil } from 'rxjs/operators';
import { UserService } from '../../services/user.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ClientsComponent implements OnInit, OnDestroy {
  public displayedColumns: string[] = ['surename', 'firstname', 'phone', 'zipCode', 'infected', 'monitoringStatus'];
  public expandedElement: BackendClient | null;
  public dataSource = new MatTableDataSource<TenantClient>();
  public healthDepartment$ = this.userService.healthDepartment$;

  private readonly destroy$$ = new Subject();

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private apiService: ApiService,
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.healthDepartment$
      .pipe(
        filter(healthDepartment => healthDepartment != null),
        takeUntil(this.destroy$$)
      )
      .subscribe(healthDepartment => {
        this.apiService.getReport(healthDepartment.id)
          .subscribe((val: Array<TenantClient>) => {
            this.dataSource.data = val;
          });
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

  toStringSymptoms(entry: DiaryEntryDto): string {
    return entry.symptoms.map(s => s.name).join(', ');
  }

}
