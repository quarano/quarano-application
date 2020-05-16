import { TrimmedPatternValidator } from '@validators/trimmed-pattern.validator';
import {Router} from '@angular/router';
import {SnackbarService} from '@services/snackbar.service';
import {ApiService} from '@services/api.service';
import {MatDialog} from '@angular/material/dialog';
import {Component, Input, OnInit} from '@angular/core';
import {CaseActionDto} from '@models/case-action';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ConfirmationDialogComponent} from '@ui/confirmation-dialog/confirmation-dialog.component';
import { VALIDATION_PATTERNS } from '@validators/validation-patterns';

@Component({
  selector: 'app-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss']
})
export class ActionComponent implements OnInit {
  @Input() caseAction: CaseActionDto;
  formGroup: FormGroup;

  buttonTooltip = '';

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      comment: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)])
    });

    if (this.hasOnlyOpenUnresolvableAnomalies()) {
      this.buttonTooltip = 'Es sind keine Auffälligkeiten offen, die manuell beendet werden können. ' +
        'Die angezeigten Hinweise werden durch das System entfernt, wenn sie bearbeitet werden.';
    }
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
    this.apiService.resolveAnomalies(this.getResolveLink(), this.formGroup.controls.comment.value)
      .subscribe(_ => {
        this.snackbarService.success('Die aktuellen Auffälligkeiten wurden als bearbeitet gekennzeichnet');
        this.router.navigate(['/tenant-admin/actions']);
      });
  }

  hasOpenResolvableAnomalies(): boolean {
    return this.getResolveLink() !== undefined
      && this.hasOpenAnomalies();
  }

  hasOnlyOpenUnresolvableAnomalies(): boolean {
    return this.getResolveLink() === undefined
      && this.hasOpenAnomalies();
  }

  public hasOpenAnomalies() {
    return (this.caseAction.anomalies.health.length + this.caseAction.anomalies.process.length) > 0
  }

  private getResolveLink() {
    return (this.caseAction._links) ? this.caseAction._links.resolve : undefined;
  }

  isButtonDisabled() {
    return this.formGroup.invalid || this.hasOnlyOpenUnresolvableAnomalies();
  }
}
