import { BadRequestService } from '@qro/shared/ui-error';
import { SubSink } from 'subsink';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Moment } from 'moment';
import { EncounterEntry, EnrollmentService } from '@qro/client/domain';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { ContactPersonDto } from '@qro/client/domain';
import { ArrayValidator, ValidationErrorService } from '@qro/shared/util-forms';
import { ContactDialogService } from '@qro/client/ui-contact-person-detail';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'qro-forgotten-contact-dialog',
  templateUrl: './forgotten-contact-dialog.component.html',
  styleUrls: ['./forgotten-contact-dialog.component.scss'],
})
export class ForgottenContactDialogComponent implements OnInit, OnDestroy {
  encounters: EncounterEntry[] = [];
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  formGroup: FormGroup;
  private subs = new SubSink();
  loading = false;

  constructor(
    private matDialogRef: MatDialogRef<ForgottenContactDialogComponent>,
    private formBuilder: FormBuilder,
    private enrollmentService: EnrollmentService,
    private snackbarService: TranslatedSnackbarService,
    private badRequestService: BadRequestService,
    private dialogService: ContactDialogService,
    public validationErrorService: ValidationErrorService,
    @Inject(MAT_DIALOG_DATA)
    public data: { contactPersons: ContactPersonDto[] }
  ) {}

  ngOnInit() {
    this.buildForm();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForm() {
    this.formGroup = this.formBuilder.group({
      day: new FormControl(null, [Validators.required]),
      contactIds: new FormControl([], [ArrayValidator.minLengthArray(1)]),
    });
  }

  cancel() {
    this.matDialogRef.close();
  }

  onSubmitted() {
    if (this.formGroup.valid) {
      this.loading = true;
      const day = (this.formGroup.controls.day.value as Moment).toDate();
      // ToDo: CORE-337 Anpassen, sobald Encounter-Endpunkte korrekt funktionieren
      // this.subs.add(
      //   this.enrollmentService
      //     .createEncounters(day, this.formGroup.controls.contactIds.value)
      //     .subscribe(
      //       () => {
      //         this.snackbarService.success('FORGOTTEN_CONTACT_DIALOG.RETROSPEKTIVE_KONTAKTE_GESPEICHERT', {
      //           value: entries.length.toString(),
      //         })
      //         this.matDialogRef.close();
      //       },
      //       (error) => {
      //         this.badRequestService.handleBadRequestError(error, this.formGroup);
      //       }
      //     )
      //     .add(() => (this.loading = false))
      // );
    }
  }

  openContactDialog() {
    this.subs.add(
      this.dialogService
        .openContactPersonDialog({ disableClose: true })
        .afterClosed()
        .subscribe((createdContact: ContactPersonDto | null) => {
          if (createdContact) {
            this.data.contactPersons.push(createdContact);
            this.formGroup.get('contactIds').patchValue([...this.formGroup.get('contactIds').value, createdContact.id]);
          }
        })
    );
  }
}
