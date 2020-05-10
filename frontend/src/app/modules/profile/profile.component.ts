import { SnackbarService } from '@services/snackbar.service';
import { EnrollmentService } from '@services/enrollment.service';
import { SubSink } from 'subsink';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { VALIDATION_PATTERNS } from '@utils/validation';
import { ClientDto } from '@models/client';
import { Moment } from 'moment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  client: ClientDto;
  private subs = new SubSink();

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private enrollmentService: EnrollmentService,
    private snackbarService: SnackbarService,
    private router: Router) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.client = data.clientData;
      this.buildForm();
    }));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForm() {
    this.formGroup = this.formBuilder.group({
      firstName: new FormControl(this.client.firstName, [Validators.required]),
      lastName: new FormControl(this.client.lastName, [Validators.required]),
      email: new FormControl(this.client.email, [Validators.required, Validators.pattern(VALIDATION_PATTERNS.email)]),
      phone: new FormControl(this.client.phone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      mobilePhone: new FormControl(this.client.mobilePhone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      street: new FormControl(this.client.street, [Validators.required]),
      houseNumber: new FormControl(this.client.houseNumber, [Validators.maxLength(6)]),
      zipCode: new FormControl(this.client.zipCode,
        [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern(VALIDATION_PATTERNS.integerUnsigned)]),
      city: new FormControl(this.client.city, [Validators.required]),
      dateOfBirth: new FormControl(this.client.dateOfBirth, [Validators.required])
    });
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
    this.subs.add(this.enrollmentService
      .updatePersonalDetails(this.client)
      .subscribe(_ => {
        this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
        this.formGroup.markAsPristine();
        this.router.navigate(['/']);
      }));
  }

}
