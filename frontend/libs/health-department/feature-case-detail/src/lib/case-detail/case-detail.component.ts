import {
  CaseActionDto,
  CaseDto,
  CaseEntityService,
  CaseStatus,
  HealthDepartmentService,
  mapCaseIdToCaseEntity,
  StartTracking,
} from '@qro/health-department/domain';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { filter, map, shareReplay, switchMap } from 'rxjs/operators';
import { cloneDeep } from 'lodash';
import { SubSink } from 'subsink';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { CloseCaseDialogComponent } from '../close-case-dialog/close-case-dialog.component';
import { ApiService, HalResponse } from '@qro/shared/util-data-access';
import { CaseType } from '@qro/auth/api';

@Component({
  selector: 'qro-case-detail',
  templateUrl: './case-detail.component.html',
  styleUrls: ['./case-detail.component.scss'],
})
export class CaseDetailComponent implements OnDestroy {
  private type$$: BehaviorSubject<CaseType> = new BehaviorSubject<CaseType>(null);
  private subs = new SubSink();

  caseId: string;
  type$: Observable<CaseType> = this.type$$.asObservable();
  caseLabel$: Observable<string>;
  ClientType = CaseType;
  caseDetail$: Observable<CaseDto>;
  caseActions$: Observable<CaseActionDto>;

  constructor(
    private route: ActivatedRoute,
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private apiService: ApiService,
    private dialog: MatDialog,
    private entityService: CaseEntityService,
    private router: Router
  ) {
    this.initData();
    this.setCaseLabel();
  }

  private setCaseLabel() {
    this.caseLabel$ = this.type$.pipe(
      map((type: CaseType) => {
        if (type === CaseType.Index) {
          return 'Index';
        }
        if (type === CaseType.Contact) {
          return 'Kontakt';
        }
      })
    );
  }

  initData(): void {
    this.caseDetail$ = combineLatest([
      this.route.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(mapCaseIdToCaseEntity(), shareReplay(1));

    this.subs.sink = this.route.paramMap.subscribe((paramMap) => {
      this.type$$.next(paramMap.get('type') as CaseType);
    });

    this.caseActions$ = this.caseDetail$.pipe(
      filter((caseDetail) => !!caseDetail.caseId),
      switchMap((caseDetail) => this.healthDepartmentService.getCaseActions(caseDetail.caseId))
    );
  }

  getStartTrackingTitle(caseDetail: CaseDto, buttonIsDisabled: boolean): string {
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

  getAnalogTrackingTitle(caseDetail: CaseDto, buttonIsDisabled: boolean): string {
    if (!buttonIsDisabled) {
      return 'Hiermit stoppen Sie die systemseitige Nachverfolgung des Falles und geben an, dass Sie den Fall manuell weiter verfolgen möchten';
    }
    if (caseDetail.status === CaseStatus.Abgeschlossen) {
      return 'Der Fall ist bereits abgeschlossen worden';
    }
    return '';
  }

  get returnLink() {
    return `/health-department/${this.type$$.value}-cases/case-list`;
  }

  startTracking(caseDetail: CaseDto) {
    this.subs.sink = this.apiService.putApiCall<StartTracking>(caseDetail, 'start-tracking').subscribe((data) => {
      this.entityService.updateOneInCache({ ...cloneDeep(caseDetail), _links: data._links });
      this.router.navigate([`/health-department/case-detail/${this.type$$.value}/${caseDetail.caseId}/email`]);
    });
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
      .pipe(switchMap(() => this.entityService.getByKey(this.caseId)))
      .subscribe((data) => {
        this.snackbarService.success('Fall abgeschlossen.');
        this.entityService.updateOneInCache(data);
      });
  }

  changeToIndexType() {
    this.type$$.next(CaseType.Index);
  }

  onChangeTypeKeyPressed(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        abortButtonText: 'Abbrechen',
        confirmButtonText: 'ok',
        title: 'Zum Indexfall machen?',
        text:
          'Sind Sie sich sicher, dass ein positiver Befund vorliegt und Sie diese Kontaktperson als Indexfall weiter bearbeiten wollen?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.changeToIndexType();
        this.router.navigate([`/health-department/case-detail/index/${this.caseId}`]);
      }
    });
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
