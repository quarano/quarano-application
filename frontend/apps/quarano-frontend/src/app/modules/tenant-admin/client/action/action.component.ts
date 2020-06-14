import { BadRequestService } from '@qro/shared/util-error';
import {
  TrimmedPatternValidator,
  VALIDATION_PATTERNS,
  ValidationErrorGenerator,
} from '@qro/shared/util-form-validation';
import { Router } from '@angular/router';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { CaseActionDto } from '../../../../models/case-action';
import { ApiService } from '../../../../services/api.service';
import { SnackbarService } from '@qro/shared/util';
import { ClientType } from '@qro/health-department/domain';
import { ConfirmDialogData, QroDialogService } from '../../../../services/qro-dialog.service';

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
    private apiService: ApiService,
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
    this.apiService
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
