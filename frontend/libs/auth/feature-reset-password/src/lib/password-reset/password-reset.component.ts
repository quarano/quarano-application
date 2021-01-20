import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '@qro/auth/domain';
import {
  ConfirmValidPasswordMatcher,
  PasswordIncludesUsernameMatcher,
  PasswordValidator,
  TrimmedPatternValidator,
  ValidationErrorService,
  VALIDATION_PATTERNS,
} from '@qro/shared/util-forms';
import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Observable } from 'rxjs';
import { delay, map, switchMap, tap } from 'rxjs/operators';
import { SubSink } from 'subsink';

@Component({
  selector: 'qro-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss'],
})
export class PasswordResetComponent implements OnInit, OnDestroy {
  formGroup: FormGroup;
  loading = false;
  private subs = new SubSink();
  public confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  public passwordIncludesUsernameMatcher = new PasswordIncludesUsernameMatcher();
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  private token$: Observable<string>;

  constructor(
    public validationErrorService: ValidationErrorService,
    private formBuilder: FormBuilder,
    private router: Router,
    private translatedSnackbarService: TranslatedSnackbarService,
    private snackbarService: SnackbarService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.createFormGroup();
    this.token$ = this.route.paramMap.pipe(map((paramMap) => paramMap.get('token')));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  createFormGroup() {
    this.formGroup = this.formBuilder.group(
      {
        username: new FormControl(null, [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(30),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.username),
        ]),
        password: new FormControl(null, [Validators.required, PasswordValidator.secure]),
        passwordConfirm: new FormControl(null, [Validators.required]),
        dateOfBirth: new FormControl(null, [Validators.required]),
      },
      {
        validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername],
      }
    );
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }

  submitForm() {
    if (this.formGroup.invalid) {
      return;
    }
    this.loading = true;
    this.subs.add(
      this.token$
        .pipe(
          switchMap((token) =>
            this.authService
              .resetPassword(this.formGroup.value, token)
              .pipe(
                switchMap((resData) => this.translatedSnackbarService.success('PASSWORD_RESET.PASSWORT_NEU_GESETZT'))
              )
          ),
          delay(3000)
        )
        .subscribe(
          (_) => {
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
