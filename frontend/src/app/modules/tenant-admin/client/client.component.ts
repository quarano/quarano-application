import { ClientService } from './../../../services/client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, ViewChild } from '@angular/core';
import { CaseDetailDto } from '@models/case-detail';
import { merge, Observable, Subject, BehaviorSubject } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { ApiService } from '@services/api.service';
import { SnackbarService } from '@services/snackbar.service';
import { CaseActionDto } from '@models/case-action';
import { MatTabGroup } from '@angular/material/tabs';
import { StartTracking } from '@models/start-tracking';
import { HalResponse } from '@models/hal-response';
import { CaseCommentDto } from '@models/case-comment';
import { ClientType } from '@models/report-case';


@Component({
  selector: 'app-clients',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit {
  caseId: string;
  type$$: BehaviorSubject<ClientType> = new BehaviorSubject<ClientType>(null);
  type$: Observable<ClientType> = this.type$$.asObservable();
  ClientType = ClientType;

  caseDetail$: Observable<CaseDetailDto>;
  caseAction$: Observable<CaseActionDto>;

  caseComments$: Observable<CaseCommentDto[]>;

  updatedDetail$$: Subject<CaseDetailDto> = new Subject<CaseDetailDto>();
  trackingStart$$: Subject<StartTracking> = new Subject<StartTracking>();

  @ViewChild('tabs', { static: false })
  tabGroup: MatTabGroup;

  tabIndex = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private clientService: ClientService) {

  }

  ngOnInit(): void {
    this.caseDetail$ = merge(
      this.route.data.pipe(map((data) => data.case)),
      this.updatedDetail$$
    ).pipe(map((data) => data));

    this.caseAction$ = this.route.data.pipe(map((data) => data.actions));
    this.caseComments$ = this.caseDetail$.pipe(map((details) => details?.comments));

    if (this.route.snapshot.queryParamMap.has('tab')) {
      this.tabIndex = Number(this.route.snapshot.queryParamMap.get('tab'));
    }
    if (this.route.snapshot.paramMap.has('id')) {
      this.caseId = this.route.snapshot.paramMap.get('id');
    }
    if (this.route.snapshot.paramMap.has('type')) {
      this.type$$.next(this.route.snapshot.paramMap.get('type') as ClientType);
    }

    this.caseDetail$.pipe(
      filter((data) => data !== null),
      filter((data) => data?._links?.hasOwnProperty('renew') && data?._links?.hasOwnProperty('start-tracking')),
      take(1)).subscribe((data) => {
        this.apiService
          .getApiCall<StartTracking>(data, 'start-tracking')
          .subscribe((sartTracking) => {
            this.trackingStart$$.next(sartTracking);
          });
      }
      );
  }

  hasOpenAnomalies(): Observable<boolean> {
    return this.caseAction$.pipe(map(a => (a.anomalies.health.length + a.anomalies.process.length) > 0));
  }

  get typeName(): Observable<string> {
    return this.type$.pipe(map(type => this.clientService.getTypeName(type)));
  }

  saveCaseData(caseDetail: CaseDetailDto) {
    let saveData$: Observable<any>;
    if (!caseDetail.caseId) {
      saveData$ = this.apiService.createCase(caseDetail, this.type$$.value);
    } else {
      saveData$ = this.apiService.updateCase(caseDetail, this.type$$.value);
    }

    saveData$.subscribe(() => {
      this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
      this.router.navigate(['/tenant-admin/clients']);
    });
  }

  startTracking(caseDetail: CaseDetailDto) {
    this.apiService.putApiCall<StartTracking>(caseDetail, 'start-tracking')
      .subscribe((data) => {
        this.trackingStart$$.next(data);
        this.tabIndex = 3;
      });
  }

  renewTracking(tracking: HalResponse) {
    this.apiService.putApiCall<StartTracking>(tracking, 'renew')
      .subscribe((data) => {
        this.trackingStart$$.next(data);
        this.tabIndex = 3;
      });
  }

  addComment(commentText: string) {
    this.apiService.addComment(this.caseId, commentText).subscribe((data) => {
      this.snackbarService.success('Kommentar erfolgreich eingetragen.');
      this.updatedDetail$$.next(data);
    });
  }

  closeCase(halResponse: HalResponse) {
    this.apiService.deleteApiCall<any>(halResponse, 'conclude').pipe(
      switchMap(() => this.apiService.getCase(this.caseId))
    ).subscribe((data) => {
      this.snackbarService.success('Fall abgeschlossen.');
      this.updatedDetail$$.next(data);

    });
  }

  changeToIndexType() {
    this.type$$.next(ClientType.Index);
  }
}
