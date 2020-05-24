import {
  ValidationErrorGenerator,
  ConfirmValidPasswordMatcher,
  TrimmedPatternValidator,
  VALIDATION_PATTERNS,
  PasswordValidator
} from '@quarano-frontend/shared/util-form-validation';
import { DataProtectionComponent } from '../../../components/data-protection/data-protection.component';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, tap } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { ApiService } from '../../../services/api.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { RegisterDto } from '../../../models/register';

@Component({
  selector: 'qro-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  loading = false;
  errorGenerator = ValidationErrorGenerator;

  public confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  private usernameIsValid = false;

  public registrationForm = new FormGroup({
    clientCode: new FormControl(null, [
      Validators.required
    ]),
    username: new FormControl(null, [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(30),
      TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.username),
      () => this.usernameIsValid ? null : { usernameInvalid: true }
    ]),
    password: new FormControl(null, [
      Validators.required, PasswordValidator.secure
    ]),
    passwordConfirm: new FormControl(null, [
      Validators.required
    ]),
    dateOfBirth: new FormControl(null, [
      Validators.required
    ]),
    dataProtectionConfirmed: new FormControl(null, [Validators.required])
  }, {
    validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername]
  });

  constructor(
    private router: Router,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private route: ActivatedRoute,
    private dialog: MatDialog) {
  }

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
    this.apiService.checkUsername(username)
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

    this.apiService.registerClient(register)
      .subscribe(
        () => {
          this.snackbarService.success(`Die Registrierung war erfolgreich. Bitte loggen Sie sich ein.`);
          this.router.navigate(['/welcome/login']);
        }
      ).add(() => this.loading = false);
  }

  openDataProtection() {
    this.dialog.open(DataProtectionComponent, { maxHeight: '95vh' });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
