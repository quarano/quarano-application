import {
  CaseDto,
  CaseEntityService,
  CaseStatus,
  HealthDepartmentService,
  StartTracking,
} from '@qro/health-department/domain';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter, map, switchMap } from 'rxjs/operators';
import { cloneDeep } from 'lodash';
import { SubSink } from 'subsink';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { CloseCaseDialogComponent } from '../close-case-dialog/close-case-dialog.component';
import { ApiService, HalResponse } from '@qro/shared/util-data-access';
import { CaseType } from '@qro/auth/api';
import { OccasionService } from '../occasion/occasion.service';
import { OccasionDto } from '../../../../domain/src/lib/model/occasion';

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
  occasions$: Observable<OccasionDto[]>;

  constructor(
    private route: ActivatedRoute,
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private apiService: ApiService,
    private dialog: MatDialog,
    private entityService: CaseEntityService,
    private router: Router,
    private occasionService: OccasionService
  ) {
    this.initData();
    this.setCaseLabel();
  }

  get isNew(): boolean {
    return !this.caseId;
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
    this.caseDetail$ = this.route.paramMap.pipe(
      switchMap((params) => this.entityService.loadOneFromStore(params.get('id')))
    );

    this.occasions$ = this.occasionService.getOccasions();

    this.subs.sink = this.route.paramMap.subscribe((paramMap) => {
      this.type$$.next(paramMap.get('type') as CaseType);
      this.caseId = paramMap.get('id');
    });
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
    this.subs.sink = this.apiService
      .putApiCall<StartTracking>(caseDetail, 'start-tracking')
      .pipe(switchMap((result) => this.entityService.getByKey(caseDetail.caseId)))
      .subscribe((caseDto) => {
        this.router.navigate([`/health-department/case-detail/${this.type$$.value}/${caseDto.caseId}/comments`]);
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
        this.entityService.updateOneInCache(data);
        this.snackbarService.success('Fall abgeschlossen.');
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
