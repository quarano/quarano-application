import { MatInput } from '@angular/material/input';
import { roles, IRole } from '@models/role';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { AccountDto } from '@models/account';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiService } from '@services/api.service';
import { SnackbarService } from '@services/snackbar.service';
import { Observable } from 'rxjs';
import { PasswordValidator } from '@validators/password-validator';
import { tap, finalize, distinctUntilChanged, debounceTime } from 'rxjs/operators';
import { ArrayValidator } from '@validators/array-validator';
import { VALIDATION_PATTERNS } from '@validators/validation-patterns';
import { TrimmedPatternValidator } from '@validators/trimmed-pattern.validator';

@Component({
  selector: 'app-account-edit',
  templateUrl: './account-edit.component.html',
  styleUrls: ['./account-edit.component.scss']
})
export class AccountEditComponent implements OnInit, OnDestroy {
  account: AccountDto;
  private subs = new SubSink();
  formGroup: FormGroup;
  private usernameIsValid = false;
  roles: IRole[] = roles.filter(r => r.isHealthDepartmentUser);

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.account = data.account;
      if (!this.isNew) { this.usernameIsValid = true; }
    }));
    this.buildForm();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return this.formGroup.pristine;
  }

  buildForm() {
    this.formGroup = this.formBuilder.group(
      {
        firstName: new FormControl(
          this.account.firstName,
          [Validators.required, TrimmedPatternValidator.match(VALIDATION_PATTERNS.name)]),
        lastName: new FormControl(
          this.account.lastName,
          [Validators.required, TrimmedPatternValidator.match(VALIDATION_PATTERNS.name)]),
        password: new FormControl({ value: null, disabled: !this.isNew }, [
          PasswordValidator.secure
        ]),
        passwordConfirm: new FormControl({ value: null, disabled: !this.isNew }, [
          Validators.required
        ]),
        username: new FormControl(this.account.username, [
          Validators.required,
          () => this.usernameIsValid ? null : { usernameInvalid: true }
        ]),
        email: new FormControl(this.account.email, [
          Validators.required,
          TrimmedPatternValidator.match(VALIDATION_PATTERNS.email)]),
        roles: new FormControl(this.account.roles, [ArrayValidator.minLengthArray(1)])
      }, {
      validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername]
    }
    );
    this.formGroup.controls.username.valueChanges
      .pipe(distinctUntilChanged(), debounceTime(1000))
      .subscribe(value => this.changeUsername(value));
  }

  get isNew(): boolean {
    return this.account?.accountId == null;
  }

  public changeUsername(username: string) {
    if (this.isNew || username !== this.account.username) {
      this.subs.add(this.apiService.checkUsername(username)
        .pipe(
          tap(() => {
            this.formGroup.get('username').disable();
          }),
          finalize(() => {
            this.formGroup.get('username').enable();
          })
        )
        .subscribe(
          (result) => {
            this.usernameIsValid = result;
          },
          () => {
            this.usernameIsValid = false;
          }
        ));
    }
  }

  public submitForm() {
    if (this.formGroup.invalid) {
      this.snackbarService.warning('Bitte alle Pflichtfelder ausfüllen.');
      return;
    }
    Object.assign(this.account, this.formGroup.value);

    if (this.isNew) {
      this.createAccount();
    } else {
      this.editAccount();
    }
  }

  private createAccount() {
    this.apiService.createHealthDepartmentUser(this.account)
      .subscribe(
        () => {
          this.snackbarService.success(`Der Account ${this.account.firstName} ${this.account.lastName} wurde erfolgreich angelegt`);
          this.formGroup.markAsPristine();
          this.router.navigate(['/administration/accounts']);
        }
      );
  }

  private editAccount() {
    this.apiService.editHealthDepartmentUser(this.account)
      .subscribe(
        () => {
          this.snackbarService.success(`Die Accountdaten für ${this.account.firstName} ` +
            `${this.account.lastName} wurden erfolgreich aktualisiert`);
          this.formGroup.markAsPristine();
          this.router.navigate(['/administration/accounts']);
        }
      );
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }
}
