import { switchMap } from 'rxjs/operators';
import { ClientService } from '@qro/client/domain';
import { BadRequestService } from '@qro/shared/ui-error';
import { SubSink } from 'subsink';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Moment } from 'moment';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { VALIDATION_PATTERNS, PhoneOrMobilePhoneValidator, TrimmedPatternValidator } from '@qro/shared/util-forms';
import { ClientDto } from '@qro/auth/api';

@Component({
  selector: 'qro-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  client: ClientDto;
  private subs = new SubSink();

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private snackbarService: TranslatedSnackbarService,
    private router: Router,
    private badRequestService: BadRequestService,
    private clientService: ClientService
  ) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.client = data.clientData;
        this.buildForm();
      })
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForm() {
    this.formGroup = this.formBuilder.group(
      {
        firstName: new FormControl(this.client.firstName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        lastName: new FormControl(this.client.lastName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        email: new FormControl(this.client.email, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
        ]),
        phone: new FormControl(this.client.phone, [
          Validators.minLength(5),
          Validators.maxLength(17),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
        ]),
        mobilePhone: new FormControl(this.client.mobilePhone, [
          Validators.minLength(5),
          Validators.maxLength(17),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
        ]),
        street: new FormControl(this.client.street, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
        ]),
        houseNumber: new FormControl(this.client.houseNumber, [
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber),
        ]),
        zipCode: new FormControl(this.client.zipCode, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
        ]),
        city: new FormControl(this.client.city, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city),
        ]),
        dateOfBirth: new FormControl(this.client.dateOfBirth, [Validators.required]),
      },
      { validators: [PhoneOrMobilePhoneValidator] }
    );
  }

  get dateOfBirth() {
    const dateValue = this.formGroup?.controls.dateOfBirth.value;
    if (dateValue?.toDate instanceof Function) {
      return (dateValue as Moment).toDate();
    }
    if (dateValue) {
      return dateValue as Date;
    }
    return null;
  }

  onSubmit() {
    if (this.formGroup.valid) {
      Object.assign(this.client, this.formGroup.value);
      this.client.dateOfBirth = this.dateOfBirth;
      this.modifyProfile();
    }
  }

  modifyProfile() {
    this.subs.add(
      this.clientService
        .updatePersonalDetails(this.client)
        .pipe(switchMap((_) => this.snackbarService.success('PROFILE.PERSÖNLICHE_DATEN_AKTUALISIERT')))
        .subscribe(
          (_) => {
            this.formGroup.markAsPristine();
            this.router.navigate(['/']);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
    );
  }
}
