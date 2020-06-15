import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorGenerator, VALIDATION_PATTERNS, TrimmedPatternValidator } from '@qro/shared/util-forms';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { CaseActionDto, HealthDepartmentService } from '@qro/health-department/domain';
import { ClientType } from '@qro/auth/api';
import {
  ConfirmDialogData,
  QroDialogService,
} from '../../../../../../../apps/quarano-frontend/src/app/services/qro-dialog.service';

@Component({
  selector: 'qro-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss'],
})
export class ActionComponent implements OnInit {
  @Input() caseAction: CaseActionDto;
  @Input() type: ClientType;
  formGroup: FormGroup;
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: QroDialogService,
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private router: Router,
    private badRequestService: BadRequestService
  ) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      comment: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)]),
    });
  }

  submitForm() {
    const data: ConfirmDialogData = {
      title: 'Auffälligkeiten abschließen?',
      text:
        'Möchten Sie die aktuellen Auffälligkeiten tatsächlich als abgeschlossen kennzeichnen? ' +
        'Diese werden dann in der Aktionsübersicht nicht mehr angezeigt.',
    };

    if (this.formGroup.valid) {
      this.dialog
        .openConfirmDialog({ data: data })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.resolveAnomalies();
          }
        });
    }
  }

  private resolveAnomalies() {
    this.healthDepartmentService
      .resolveAnomalies(
        this.caseAction._links.resolve,
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

  hasOpenAnomalies(): boolean {
    return this.caseAction.anomalies.health.length + this.caseAction.anomalies.process.length > 0;
  }

  get returnLink() {
    return `/health-department/${this.type}-cases/action-list`;
  }
}
