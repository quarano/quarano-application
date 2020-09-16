import { Component, OnInit, Inject, OnDestroy, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { StoredContactLocationDto, ContactLocationService, TransientContactLocationDto } from '@qro/client/domain';
import { TrimmedPatternValidator, VALIDATION_PATTERNS, ValidationErrorService } from '@qro/shared/util-forms';
import { SubSink } from 'subsink';
import { Observable } from 'rxjs';

@Component({
  selector: 'qro-contact-location-form',
  templateUrl: './contact-location-form.component.html',
  styleUrls: ['./contact-location-form.component.scss'],
})
export class ContactLocationFormComponent implements OnInit, OnDestroy {
  public formGroup: FormGroup;
  public viewOnly = false;
  private subs = new SubSink();
  @Output() public newLocation = new EventEmitter<StoredContactLocationDto>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: StoredContactLocationDto & TransientContactLocationDto,
    private dialogRef: MatDialogRef<ContactLocationFormComponent>,
    private contactLocationService: ContactLocationService,
    private validationErrorService: ValidationErrorService
  ) {
    // TODO reuse this form to show user input to GA
    if (this.data && this.data.id) {
      this.viewOnly = true;
    } else {
      // TODO remove hack?
      this.data = Object.create(null);
    }
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup({
      name: new FormControl(this.data.name, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)]),
      street: new FormControl(this.data.street, [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
      ]),
      houseNumber: new FormControl(this.data.houseNumber, [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber),
      ]),
      zipCode: new FormControl(this.data.zipCode, [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
      ]),
      city: new FormControl(this.data.houseNumber, [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city),
      ]),
      contactPerson: new FormControl(this.data.contactPerson, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      startTime: new FormControl(this.data.startTime, [Validators.required]),
      endTime: new FormControl(this.data.endTime, [Validators.required]),
      notes: new FormControl(this.data.notes, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)]),
    });
  }

  saveLocation() {
    if (this.formGroup.valid) {
      this.subs.add(
        this.contactLocationService.createContactLocation(this.formGroup.value).subscribe((createdLocation) => {
          this.data = createdLocation;
          this.newLocation.emit(createdLocation);
          this.dialogRef.close(createdLocation);
        })
      );
    } else {
      // Notify validation errors
      this.formGroup.markAllAsTouched();
    }
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  getErrorKeys(formControlName: string): string[] {
    if (this.viewOnly) {
      return null; // Don't show errors on GA view
    }
    return this.validationErrorService.getErrorKeys(this.formGroup.controls[formControlName]);
  }

  getErrorMessage(formControlName, errorKey): Observable<string> {
    return this.validationErrorService.getErrorMessage(this.formGroup.controls[formControlName], errorKey);
  }
}
