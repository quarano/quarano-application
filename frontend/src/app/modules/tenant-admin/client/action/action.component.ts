import { Router } from '@angular/router';
import { SnackbarService } from '@services/snackbar.service';
import { ApiService } from '@services/api.service';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit, Input } from '@angular/core';
import { CaseActionDto } from '@models/case-action';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { ConfirmationDialogComponent } from '@ui/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-client-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss']
})
export class ActionComponent implements OnInit {
  @Input() caseAction: CaseActionDto;
  formGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      comment: new FormControl()
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
    this.apiService.resolveAnomalies(this.caseAction._links.resolve, this.formGroup.controls.comment.value)
      .subscribe(_ => {
        this.snackbarService.success('Die aktuellen Auffälligkeiten wurden als bearbeitet gekennzeichnet');
        this.router.navigate(['/tenant-admin/actions']);
      });
  }
}
