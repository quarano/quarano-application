import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {BehaviorSubject, merge, Observable, Subject} from 'rxjs';
import {filter, map, switchMap, take} from 'rxjs/operators';
import {CloseCaseDialogComponent} from './close-case-dialog/close-case-dialog.component';
import {cloneDeep} from 'lodash';
import {SubSink} from 'subsink';
import {CaseActionDto} from '../../../models/case-action';
import {CaseCommentDto} from '../../../models/case-comment';
import {CaseDetailDto} from '../../../models/case-detail';
import {StartTracking} from '../../../models/start-tracking';
import {MatTabGroup} from '@angular/material/tabs';
import {ApiService} from '../../../services/api.service';
import {SnackbarService} from '../../../services/snackbar.service';
import {HalResponse} from '../../../models/hal-response';
import {ConfirmationDialogComponent} from '../../../ui/confirmation-dialog/confirmation-dialog.component';
import {ClientType} from '@quarano-frontend/health-department/domain';
import {SymptomDto} from '../../../models/symptom';
import {QuestionnaireDto} from '../../../models/first-query';
import {ContactDto} from '../../../models/contact';

@Component({
  selector: 'qro-clients',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit, OnDestroy {
  caseId: string;
  type$$: BehaviorSubject<ClientType> = new BehaviorSubject<ClientType>(null);
  type$: Observable<ClientType> = this.type$$.asObservable();
  ClientType = ClientType;
  commentLoading = false;
  personalDataLoading = false;

  caseDetail$: Observable<CaseDetailDto>;
  caseAction$: Observable<CaseActionDto>;
  caseIndexContacts$: Observable<ContactDto[]>;

  caseComments$: Observable<CaseCommentDto[]>;

  symptoms$: Observable<SymptomDto[]>;

  updatedDetail$$: Subject<CaseDetailDto> = new Subject<CaseDetailDto>();
  trackingStart$$: Subject<StartTracking> = new Subject<StartTracking>();
  questionnaire$$: Subject<QuestionnaireDto> = new Subject<QuestionnaireDto>();



  @ViewChild('tabs', { static: false })
  tabGroup: MatTabGroup;

  tabIndex = 0;

  private subs = new SubSink();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.caseDetail$ = merge(
      this.route.data.pipe(map((data) => data.case)),
      this.updatedDetail$$
    ).pipe(map((data) => data));

    this.caseAction$ = this.route.data.pipe(map((data) => data.actions));
    this.caseComments$ = this.caseDetail$.pipe(map((details) => details?.comments));
    this.caseIndexContacts$ = this.caseDetail$.pipe(
      map(details => details?.indexContacts)
    );

    if (this.route.snapshot.queryParamMap.has('tab')) {
      this.tabIndex = Number(this.route.snapshot.queryParamMap.get('tab'));
    }
    if (this.route.snapshot.paramMap.has('id')) {
      this.caseId = this.route.snapshot.paramMap.get('id');
    }
    if (this.route.snapshot.paramMap.has('type')) {
      this.type$$.next(this.route.snapshot.paramMap.get('type') as ClientType);
    }

    this.subs.sink = this.caseDetail$.pipe(
      filter((data) => data !== null),
      filter((data) => data?._links?.hasOwnProperty('renew')),
      take(1)).subscribe((data) => {
        this.subs.sink = this.apiService
          .getApiCall<StartTracking>(data, 'renew')
          .subscribe((startTracking) => {
            this.trackingStart$$.next(startTracking);
          });
      }
    );

    this.subs.sink = this.caseDetail$.pipe(
      filter((data) => data !== null),
      filter((data) => data?._links?.hasOwnProperty('questionnaire')),
      take(1)).subscribe((data) => {
      this.subs.sink = this.apiService
        .getApiCall<QuestionnaireDto>(data, 'questionnaire')
        .subscribe((questionnaire) => {
          this.questionnaire$$.next(questionnaire);
          this.symptoms$ = this.route.data.pipe(
            map((resolver) => resolver.symptoms),
            map((symptoms: SymptomDto[] ) =>
              symptoms.filter((symptom) => questionnaire.symptoms
                .findIndex((symptomId) => symptomId === symptom.id) !== -1)
            )
          );
        });
    });
  }

  hasOpenAnomalies(): Observable<boolean> {
    return this.caseAction$.pipe(map(a => (a.anomalies.health.length + a.anomalies.process.length) > 0));
  }

  saveCaseData(caseDetail: CaseDetailDto) {
    this.personalDataLoading = true;
    let saveData$: Observable<any>;
    if (!caseDetail.caseId) {
      saveData$ = this.apiService.createCase(caseDetail, this.type$$.value);
    } else {
      saveData$ = this.apiService.updateCase(caseDetail);
    }

    this.subs.sink = saveData$.subscribe(() => {
      this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
      this.router.navigate([this.returnLink]);
    }).add(() => this.personalDataLoading = false);
  }

  get returnLink() {
    return `/health-department/${this.type$$.value}-cases/case-list`;
  }

  startTracking(caseDetail: CaseDetailDto) {
    this.subs.sink = this.apiService.putApiCall<StartTracking>(caseDetail, 'start-tracking')
      .subscribe((data) => {
        this.trackingStart$$.next(data);
        this.updatedDetail$$.next({ ...cloneDeep(caseDetail), _links: data._links });

        this.tabIndex = 3;
      });
  }

  renewTracking(tracking: HalResponse) {
    this.subs.sink = this.apiService.putApiCall<StartTracking>(tracking, 'renew')
      .subscribe((data) => {
        this.trackingStart$$.next(data);
        this.tabIndex = 3;
      });
  }

  addComment(commentText: string) {
    this.commentLoading = true
    this.subs.sink = this.apiService.addComment(this.caseId, commentText)
      .subscribe((data) => {
        this.snackbarService.success('Kommentar erfolgreich eingetragen.');
        this.updatedDetail$$.next(data);
      }).add(() => this.commentLoading = false);
  }

  checkForClose(halResponse: HalResponse) {
    this.subs.sink = this.dialog.open(CloseCaseDialogComponent, { width: '640px' }).afterClosed().pipe(
      filter((comment) => comment),
      switchMap((comment: string) => this.apiService.addComment(this.caseId, comment)),
      map(() => this.closeCase(halResponse))
    ).subscribe();
  }

  closeCase(halResponse: HalResponse) {
    this.subs.sink = this.apiService.deleteApiCall<any>(halResponse, 'conclude').pipe(
      switchMap(() => this.apiService.getCase(this.caseId))
    ).subscribe((data) => {
      this.snackbarService.success('Fall abgeschlossen.');
      this.updatedDetail$$.next(data);

    });
  }

  changeToIndexType() {
    this.type$$.next(ClientType.Index);
  }

  onChangeTypeKeyPressed(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Zum Indexfall machen?',
        text:
          'Sind Sie sich sicher, dass ein positiver Befund vorliegt und Sie diesen Kontaktfall als Indexfall weiter bearbeiten wollen?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.changeToIndexType();
      }
    });
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
