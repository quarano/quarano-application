import { ApiService } from '@qro/shared/util-data-access';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit, Input } from '@angular/core';
import { CaseEntityService, ContactListItemDto } from '@qro/health-department/domain';
import { Observable, combineLatest } from 'rxjs';
import { map, switchMap, shareReplay } from 'rxjs/operators';

interface RowViewModel {
  firstName: string;
  lastName: string;
  isHealthStaff: string;
  isSenior: string;
  hasPreExistingConditions: string;
  lastContact: Date;
  status: string;
  caseType: string;
  caseId: string;
}

@Component({
  selector: 'qro-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.scss'],
})
export class ContactListComponent implements OnInit {
  caseName$: Observable<string>;
  rows$: Observable<RowViewModel[]>;

  constructor(
    private router: Router,
    private snackbarService: SnackbarService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private entityService: CaseEntityService
  ) {}

  ngOnInit() {
    const caseDetail$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      map(([id, entityMap]) => {
        return entityMap[id];
      }),
      shareReplay(1)
    );

    this.caseName$ = caseDetail$.pipe(map((detail) => `${detail.firstName} ${detail.lastName}`));

    this.rows$ = caseDetail$.pipe(
      switchMap((detail) => {
        if (detail._links.hasOwnProperty('contacts')) {
          return this.apiService
            .getApiCall<any>(detail, 'contacts')
            .pipe(map((result) => result?._embedded?.contacts?.map((c) => this.getRowData(c))));
        }
      })
    );
  }

  onSelect(event) {
    const selectedItem = event?.selected[0] as RowViewModel;
    if (selectedItem?.caseId && selectedItem?.caseType) {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigate(['/health-department/case-detail', selectedItem.caseType, selectedItem.caseId]);
    } else {
      this.snackbarService.message(`Zu ${selectedItem.firstName} ${selectedItem.lastName} liegt noch kein Fall vor.`);
    }
  }

  private getRowData(listItem: ContactListItemDto): RowViewModel {
    return {
      firstName: listItem.firstName,
      lastName: listItem.lastName,
      status: listItem.caseStatusLabel,
      isHealthStaff: this.getBooleanText(listItem.isHealthStaff),
      isSenior: this.getBooleanText(listItem.isSenior),
      hasPreExistingConditions: this.getBooleanText(listItem.hasPreExistingConditions),
      caseId: listItem.caseId,
      caseType: listItem.caseType,
      lastContact: listItem.contactDates.length > 0 ? new Date(listItem.contactDates[0]) : null,
    };
  }

  private getBooleanText(value: boolean): string {
    if (value) {
      return 'ja';
    }
    if (value === false) {
      return 'nein';
    }
    return '?';
  }
}
