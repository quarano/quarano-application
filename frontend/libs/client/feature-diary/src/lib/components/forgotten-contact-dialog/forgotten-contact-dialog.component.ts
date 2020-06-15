import { BadRequestService } from '@qro/shared/ui-error';
import { SubSink } from 'subsink';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Moment } from 'moment';
import { EncounterEntry, EnrollmentService } from '@qro/client/domain';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ContactPersonDto } from '@qro/client/domain';
import { ArrayValidator, QroDialogService, ValidationErrorGenerator } from '@qro/shared/util-forms';

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
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private matDialogRef: MatDialogRef<ForgottenContactDialogComponent>,
    private formBuilder: FormBuilder,
    private enrollmentService: EnrollmentService,
    private snackbarService: SnackbarService,
    private badRequestService: BadRequestService,
    private dialogService: QroDialogService,
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
      this.subs.add(
        this.enrollmentService
          .createEncounters(day, this.formGroup.controls.contactIds.value)
          .subscribe(
            (entries) => {
              this.snackbarService.success(`${entries.length} retrospektive Kontakte erfolgreich gespeichert`);
              this.matDialogRef.close();
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, this.formGroup);
            }
          )
          .add(() => (this.loading = false))
      );
    }
  }

  openContactDialog() {
    this.subs.add(
      this.dialogService
        .openContactPersonDialog()
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
