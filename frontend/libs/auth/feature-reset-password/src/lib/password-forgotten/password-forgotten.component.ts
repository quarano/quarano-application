import { AuthService } from '@qro/auth/domain';
import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Router } from '@angular/router';
import { BadRequestService } from '@qro/shared/ui-error';
import { TrimmedPatternValidator, ValidationErrorService, VALIDATION_PATTERNS } from '@qro/shared/util-forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { SubSink } from 'subsink';
import { switchMap, delay, tap } from 'rxjs/operators';

@Component({
  selector: 'qro-password-forgotten',
  templateUrl: './password-forgotten.component.html',
  styleUrls: ['./password-forgotten.component.scss'],
})
export class PasswordForgottenComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  loading = false;
  private subs = new SubSink();

  constructor(
    public validationErrorService: ValidationErrorService,
    private formBuilder: FormBuilder,
    private badRequestService: BadRequestService,
    private router: Router,
    private translatedSnackbarService: TranslatedSnackbarService,
    private snackbarService: SnackbarService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.createFormGroup();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  createFormGroup() {
    this.formGroup = this.formBuilder.group({
      username: new FormControl(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(30),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.username),
      ]),
      email: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email)]),
    });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }

  submitForm() {
    if (this.formGroup.invalid) {
      return;
    }
    this.loading = true;
    const { email, username } = this.formGroup.value;
    this.subs.add(
      this.authService
        .requestPasswordResetToken(username, email)
        .pipe(
          tap((_) => (this.loading = false)),
          delay(3000)
        )
        .subscribe(
          (_) => {
            this.translatedSnackbarService.success('PASSWORD_FORGOTTEN.EMAIL_GESENDET');
            this.router.navigate(['/']);
          },
          (error) => {
            this.snackbarService.error(error.errors);
          }
        )
        .add(() => (this.loading = false))
    );
  }
}
