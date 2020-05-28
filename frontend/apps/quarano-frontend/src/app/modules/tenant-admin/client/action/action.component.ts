import { BadRequestService } from '@qro/shared/util-error';
import {
  ValidationErrorGenerator,
  VALIDATION_PATTERNS,
  TrimmedPatternValidator
} from '@qro/shared/util-form-validation';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { CaseActionDto } from '../../../../models/case-action';
import { ApiService } from '../../../../services/api.service';
import { SnackbarService } from '@qro/shared/util';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { ClientType } from '@qro/health-department/domain';

@Component({
  selector: 'qro-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss']
})
export class ActionComponent implements OnInit {
  @Input() caseAction: CaseActionDto;
  @Input() type: ClientType;
  formGroup: FormGroup;
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router,
    private badRequestService: BadRequestService) { }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      comment: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)])
    });
  }

  submitForm() {
    if (this.formGroup.valid) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          title: 'Auffälligkeiten abschließen?',
          text:
            'Möchten Sie die aktuellen Auffälligkeiten tatsächlich als abgeschlossen kennzeichnen? ' +
            'Diese werden dann in der Aktionsübersicht nicht mehr angezeigt.'
        }
      });

      dialogRef.afterClosed()
        .subscribe(result => {
          if (result) {
            this.resolveAnomalies();
          }
        });
    }
  }

  private resolveAnomalies() {
    this.apiService.resolveAnomalies(
      this.caseAction._links.resolve,
      this.formGroup.controls.comment.value?.trim() || 'Auffälligkeiten geprüft und als erledigt markiert')
      .subscribe(_ => {
        this.snackbarService.success('Die aktuellen Auffälligkeiten wurden als erledigt gekennzeichnet');
        this.router.navigate([this.returnLink]);
      }, error => {
        this.badRequestService.handleBadRequestError(error, this.formGroup);
      });
  }

  hasOpenAnomalies(): boolean {
    return (this.caseAction.anomalies.health.length + this.caseAction.anomalies.process.length) > 0;
  }

  get returnLink() {
    return `/health-department/${this.type}-cases/action-list`;
  }
}
