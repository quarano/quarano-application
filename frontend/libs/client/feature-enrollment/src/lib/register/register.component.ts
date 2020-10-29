import { SubSink } from 'subsink';
import { AuthService } from '@qro/auth/api';
import { BadRequestService } from '@qro/shared/ui-error';
import {
  ValidationErrorService,
  ConfirmValidPasswordMatcher,
  TrimmedPatternValidator,
  VALIDATION_PATTERNS,
  PasswordValidator,
  PasswordIncludesUsernameMatcher,
} from '@qro/shared/util-forms';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, tap, map, switchMap } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { RegisterDto, EnrollmentService } from '@qro/client/domain';
import { DataProtectionDialogComponent } from '../data-protection-dialog/data-protection-dialog.component';
import { TokenService, AuthStore } from '@qro/auth/domain';

@Component({
  selector: 'qro-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  loading = false;
  private subs = new SubSink();

  public confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  public passwordIncludesUsernameMatcher = new PasswordIncludesUsernameMatcher();
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
    private snackbarService: TranslatedSnackbarService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private badRequestService: BadRequestService,
    private authService: AuthService,
    private tokenService: TokenService,
    public validationErrorService: ValidationErrorService,
    private authStore: AuthStore
  ) {}

  ngOnInit(): void {
    this.checkUrlCode();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
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
      this.subs.add(this.snackbarService.warning('REGISTER.BITTE_ALLE_PFLICHTFELDER_AUSFÜLLEN').subscribe());
      return;
    }
    this.loading = true;
    const register: RegisterDto = Object.assign(this.registrationForm.value);
    register.dateOfBirth = this.registrationForm.controls.dateOfBirth.value.toDate();

    this.subs.add(
      this.enrollmentService
        .registerClient(register)
        .pipe(
          tap((res) => this.authStore.login()),
          tap((res) => this.tokenService.setToken(res.headers.get('X-Auth-Token'))),
          switchMap((res) => this.snackbarService.success('REGISTER.REGISTRIERUNG_ERFOLGREICH').pipe(map((r) => res)))
        )
        .subscribe(
          (res) => {
            this.router.navigate(['/client/diary/diary-list']);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.registrationForm);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  openDataProtection() {
    this.dialog.open(DataProtectionDialogComponent, { maxHeight: '100vh' });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
