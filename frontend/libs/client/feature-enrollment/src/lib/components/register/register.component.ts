import { AuthService } from '@qro/auth/api';
import { BadRequestService } from '@qro/shared/ui-error';
import {
  ValidationErrorGenerator,
  ConfirmValidPasswordMatcher,
  TrimmedPatternValidator,
  VALIDATION_PATTERNS,
  PasswordValidator,
} from '@qro/shared/util-form-validation';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, tap } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { SnackbarService } from '@qro/shared/util';
import { RegisterDto, EnrollmentService } from '@qro/client/domain';
import { DataProtectionDialogComponent } from '../data-protection-dialog/data-protection-dialog.component';

@Component({
  selector: 'qro-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  loading = false;
  errorGenerator = ValidationErrorGenerator;

  public confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  private usernameIsValid = false;

  public registrationForm = new FormGroup(
    {
      clientCode: new FormControl(null, [Validators.required]),
      username: new FormControl(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(30),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.username),
        () => (this.usernameIsValid ? null : { usernameInvalid: true }),
      ]),
      password: new FormControl(null, [Validators.required, PasswordValidator.secure]),
      passwordConfirm: new FormControl(null, [Validators.required]),
      dateOfBirth: new FormControl(null, [Validators.required]),
      dataProtectionConfirmed: new FormControl(null, [Validators.required]),
    },
    {
      validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername],
    }
  );

  constructor(
    private router: Router,
    private enrollmentService: EnrollmentService,
    private snackbarService: SnackbarService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private badRequestService: BadRequestService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.checkUrlCode();
  }

  private checkUrlCode() {
    const clientCode = this.route.snapshot.paramMap.get('clientcode');

    if (clientCode) {
      this.registrationForm.get('clientCode').setValue(clientCode);
      this.registrationForm.get('clientCode').updateValueAndValidity();
    }
  }

  public changeUsername() {
    const username = this.registrationForm.controls.username.value;
    this.authService
      .checkUsername(username)
      .pipe(
        tap(() => {
          this.registrationForm.get('username').disable();
        }),
        finalize(() => {
          this.registrationForm.get('username').enable();
        })
      )
      .subscribe(
        (result) => {
          this.usernameIsValid = result;
        },
        () => {
          this.usernameIsValid = false;
        }
      );
  }

  public submitForm() {
    if (this.registrationForm.invalid) {
      this.snackbarService.warning('Bitte alle Pflichtfelder ausfüllen.');
      return;
    }
    this.loading = true;
    const register: RegisterDto = Object.assign(this.registrationForm.value);
    register.dateOfBirth = this.registrationForm.controls.dateOfBirth.value.toDate();

    this.enrollmentService
      .registerClient(register)
      .subscribe(
        () => {
          this.snackbarService.success(`Die Registrierung war erfolgreich. Bitte loggen Sie sich ein.`);
          this.router.navigate(['/all-users/login']);
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, this.registrationForm);
        }
      )
      .add(() => (this.loading = false));
  }

  openDataProtection() {
    this.dialog.open(DataProtectionDialogComponent, { maxHeight: '95vh' });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
