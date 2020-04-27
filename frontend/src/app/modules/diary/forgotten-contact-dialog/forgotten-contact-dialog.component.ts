import { SubSink } from 'subsink';
import { FormGroup, FormBuilder, FormControl, Validators, AbstractControl } from '@angular/forms';
import { EncounterEntry } from '@models/encounter';
import { SnackbarService } from '@services/snackbar.service';
import { EnrollmentService } from '@services/enrollment.service';
import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ContactPersonDto } from '@models/contact-person';
import { Moment } from 'moment';

@Component({
  selector: 'app-forgotten-contact-dialog',
  templateUrl: './forgotten-contact-dialog.component.html',
  styleUrls: ['./forgotten-contact-dialog.component.scss'],
})
export class ForgottenContactDialogComponent implements OnInit, OnDestroy {
  encounters: EncounterEntry[] = [];
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  formGroup: FormGroup;
  private subs = new SubSink();

  constructor(
    private matDialogRef: MatDialogRef<ForgottenContactDialogComponent>,
    private formBuilder: FormBuilder,
    private enrollmentService: EnrollmentService,
    private snackbarService: SnackbarService,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      contactPersons: ContactPersonDto[];
    }) { }

  ngOnInit() {
    this.buildForm();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForm() {
    this.formGroup = this.formBuilder.group({
      day: new FormControl(null, [Validators.required]),
      contactIds: new FormControl([], [this.minLengthArray(1)])
    });
  }

  minLengthArray(min: number) {
    return (c: AbstractControl): { [key: string]: any } => {
      if (c.value.length >= min) {
        return null;
      }
      return { minLengthArray: { valid: false } };
    };
  }

  cancel() {
    this.matDialogRef.close();
  }

  onSubmitted() {
    if (this.formGroup.valid) {
      const day = (this.formGroup.controls.day.value as Moment).toDate();
      this.subs.add(this.enrollmentService.createEncounters(day, this.formGroup.controls.contactIds.value)
        .subscribe(entries => {
          this.snackbarService.success(`${entries.length} retrospektive Kontakte erfolgreich gespeichert`);
          this.matDialogRef.close();
        }));
    }
  }
}
