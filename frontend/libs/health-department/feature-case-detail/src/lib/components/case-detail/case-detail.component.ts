import {
  CaseDetailDto,
  CaseActionDto,
  ContactDto,
  CaseCommentDto,
  StartTracking,
  HealthDepartmentService,
  CaseStatus,
} from '@qro/health-department/domain';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject, combineLatest, merge, Observable, Subject } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { cloneDeep } from 'lodash';
import { SubSink } from 'subsink';
import { MatTabGroup } from '@angular/material/tabs';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { QuestionnaireDto } from '@qro/shared/util-data-access';
import { SymptomDto } from '@qro/shared/util-symptom';
import { CloseCaseDialogComponent } from '../close-case-dialog/close-case-dialog.component';
import { ApiService, HalResponse } from '@qro/shared/util-data-access';
import { ClientType } from '@qro/auth/api';

@Component({
  selector: 'qro-case-detail',
  templateUrl: './case-detail.component.html',
  styleUrls: ['./case-detail.component.scss'],
})
export class CaseDetailComponent implements OnInit, OnDestroy {
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
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private apiService: ApiService,
    private dialog: QroDialogService
  ) {}

  ngOnInit(): void {
    this.caseDetail$ = merge(this.route.data.pipe(map((data) => data.case)), this.updatedDetail$$).pipe(
      map((data) => data)
    );

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

    this.caseIndexContacts$ = combineLatest([
      this.caseDetail$.pipe(map((details) => details?.indexContacts)),
      this.type$,
    ]).pipe(
      map(([indexContacts, clientType]) => {
        if (clientType === ClientType.Contact) {
          return indexContacts;
        }
        return undefined;
      })
    );

    this.subs.sink = this.caseDetail$
      .pipe(
        filter((data) => data !== null),
        filter((data) => data?._links?.hasOwnProperty('renew')),
        take(1)
      )
      .subscribe((data) => {
        this.subs.sink = this.apiService.getApiCall<StartTracking>(data, 'renew').subscribe((startTracking) => {
          this.trackingStart$$.next(startTracking);
        });
      });

    this.subs.sink = this.caseDetail$
      .pipe(
        filter((data) => data !== null),
        filter((data) => data?._links?.hasOwnProperty('questionnaire')),
        take(1)
      )
      .subscribe((data) => {
        this.subs.sink = this.apiService
          .getApiCall<QuestionnaireDto>(data, 'questionnaire')
          .subscribe((questionnaire) => {
            this.questionnaire$$.next(questionnaire);
            this.symptoms$ = this.route.data.pipe(
              map((resolver) => resolver.symptoms),
              map((symptoms: SymptomDto[]) =>
                symptoms.filter(
                  (symptom) => questionnaire.symptoms.findIndex((symptomId) => symptomId === symptom.id) !== -1
                )
              )
            );
          });
      });
  }

  hasOpenAnomalies(): Observable<boolean> {
    return this.caseAction$.pipe(map((a) => a.anomalies.health.length + a.anomalies.process.length > 0));
  }

  getStartTrackingTitle(caseDetail: CaseDetailDto, buttonIsDisabled: boolean): string {
    console.log(caseDetail.status);
    if (caseDetail.status === CaseStatus.Abgeschlossen) {
      return 'Der Fall ist bereits abgeschlossen worden';
    }
    if (
      caseDetail.status === CaseStatus.InNachverfolgung ||
      caseDetail.status === CaseStatus.InRegistrierung ||
      caseDetail.status === CaseStatus.RegistrierungAbgeschlossen
    ) {
      return 'Nachverfolgung bereits aktiv';
    }
    if (!buttonIsDisabled) {
      return 'Sobald Sie die Person telefonisch kontaktiert haben, können Sie hier die Nachverfolgung starten';
    }
    if (caseDetail.infected) {
      return 'Um die Nachverfolgung zu starten müssen zunächst folgende Daten erfasst werden: Vorname, Nachname, Geburtsdatum, Abstrichdatum, Quarantänezeitraum, eine Telefonnummer sowie die Emailadresse';
    }
    return 'Um die Nachverfolgung zu starten müssen zunächst folgende Daten erfasst werden: Vorname, Nachname, Geburtsdatum, eine Telefonnummer sowie die Emailadresse';
  }

  getAnalogTrackingTitle(caseDetail: CaseDetailDto, buttonIsDisabled: boolean): string {
    if (!buttonIsDisabled) {
      return 'Hiermit stoppen Sie die systemseitige Nachverfolgung des Falles und geben an, dass Sie den Fall manuell weiter verfolgen möchten';
    }
    if (caseDetail.status === CaseStatus.Abgeschlossen) {
      return 'Der Fall ist bereits abgeschlossen worden';
    }
    return '';
  }

  saveCaseData(caseDetail: CaseDetailDto) {
    this.personalDataLoading = true;
    let saveData$: Observable<any>;
    if (!caseDetail.caseId) {
      saveData$ = this.healthDepartmentService.createCase(caseDetail, this.type$$.value);
    } else {
      saveData$ = this.healthDepartmentService.updateCase(caseDetail);
    }

    this.subs.sink = saveData$
      .subscribe(() => {
        this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
        this.router.navigate([this.returnLink]);
      })
      .add(() => (this.personalDataLoading = false));
  }

  get returnLink() {
    return `/health-department/${this.type$$.value}-cases/case-list`;
  }

  startTracking(caseDetail: CaseDetailDto) {
    this.subs.sink = this.apiService.putApiCall<StartTracking>(caseDetail, 'start-tracking').subscribe((data) => {
      this.trackingStart$$.next(data);
      this.updatedDetail$$.next({ ...cloneDeep(caseDetail), _links: data._links });

      this.tabIndex = 4;
    });
  }

  renewTracking(tracking: HalResponse) {
    this.subs.sink = this.apiService.putApiCall<StartTracking>(tracking, 'renew').subscribe((data) => {
      this.trackingStart$$.next(data);
      this.tabIndex = 4;
    });
  }

  addComment(commentText: string) {
    this.commentLoading = true;
    this.subs.sink = this.healthDepartmentService
      .addComment(this.caseId, commentText)
      .subscribe((data) => {
        this.snackbarService.success('Kommentar erfolgreich eingetragen.');
        this.updatedDetail$$.next(data);
      })
      .add(() => (this.commentLoading = false));
  }

  checkForClose(halResponse: HalResponse) {
    this.subs.sink = this.dialog
      .open(CloseCaseDialogComponent, { width: '640px' })
      .afterClosed()
      .pipe(
        filter((comment) => comment),
        switchMap((comment: string) => this.healthDepartmentService.addComment(this.caseId, comment)),
        map(() => this.closeCase(halResponse))
      )
      .subscribe();
  }

  closeCase(halResponse: HalResponse) {
    this.subs.sink = this.apiService
      .deleteApiCall<any>(halResponse, 'conclude')
      .pipe(switchMap(() => this.healthDepartmentService.getCase(this.caseId)))
      .subscribe((data) => {
        this.snackbarService.success('Fall abgeschlossen.');
        this.updatedDetail$$.next(data);
      });
  }

  changeToIndexType() {
    this.type$$.next(ClientType.Index);
  }

  onChangeTypeKeyPressed(): void {
    const data: ConfirmDialogData = {
      title: 'Zum Indexfall machen?',
      text:
        'Sind Sie sich sicher, dass ein positiver Befund vorliegt und Sie diese Kontaktperson als Indexfall weiter bearbeiten wollen?',
    };

    this.dialog
      .openConfirmDialog({ data: data })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.changeToIndexType();
        }
      });
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
