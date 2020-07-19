import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorGenerator, VALIDATION_PATTERNS, TrimmedPatternValidator } from '@qro/shared/util-forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { CaseActionDto, HealthDepartmentService } from '@qro/health-department/domain';
import { CaseType } from '@qro/auth/api';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss'],
})
export class ActionComponent implements OnInit {
  caseAction$: Observable<CaseActionDto>;
  formGroup: FormGroup;
  caseType: CaseType;
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private router: Router,
    private badRequestService: BadRequestService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.caseAction$ = this.route.data.pipe(map((data) => data.actions));
    this.caseType = this.route.parent.snapshot.paramMap.get('type') as CaseType;

    this.formGroup = this.formBuilder.group({
      comment: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)]),
    });
  }

  submitForm(caseAction: CaseActionDto) {
    if (this.formGroup.valid) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          abortButtonText: 'Abbrechen',
          confirmButtonText: 'ok',
          title: 'Auffälligkeiten abschließen?',
          text:
            'Möchten Sie die aktuellen Auffälligkeiten tatsächlich als abgeschlossen kennzeichnen? ' +
            'Diese werden dann in der Aktionsübersicht nicht mehr angezeigt.',
        },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.resolveAnomalies(caseAction);
        }
      });
    }
  }

  private resolveAnomalies(caseAction: CaseActionDto) {
    this.healthDepartmentService
      .resolveAnomalies(
        caseAction._links.resolve,
        this.formGroup.controls.comment.value?.trim() || 'Auffälligkeiten geprüft und als erledigt markiert'
      )
      .subscribe(
        (_) => {
          this.snackbarService.success('Die aktuellen Auffälligkeiten wurden als erledigt gekennzeichnet');
          this.router.navigate([this.returnLink]);
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, this.formGroup);
        }
      );
  }

  hasOpenAnomalies(caseAction: CaseActionDto): boolean {
    return caseAction.anomalies.health.length + caseAction.anomalies.process.length > 0;
  }

  get returnLink() {
    return `/health-department/${this.type}-cases/action-list`;
  }
}
