import { cloneDeep } from 'lodash';
import { AccountDto, AccountEntityService } from '@qro/administration/domain';
import { AuthService, roles, IRole } from '@qro/auth/api';
import { BadRequestService } from '@qro/shared/ui-error';
import { MatInput } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { tap, finalize, distinctUntilChanged, debounceTime, switchMap, shareReplay } from 'rxjs/operators';
import { SnackbarService } from '@qro/shared/util-snackbar';
import {
  ArrayValidator,
  PasswordValidator,
  VALIDATION_PATTERNS,
  TrimmedPatternValidator,
  ConfirmValidPasswordMatcher,
  ValidationErrorService,
  PasswordIncludesUsernameMatcher,
} from '@qro/shared/util-forms';

@Component({
  selector: 'qro-account-edit',
  templateUrl: './account-edit.component.html',
  styleUrls: ['./account-edit.component.scss'],
})
export class AccountEditComponent implements OnInit, OnDestroy {
  account$: Observable<AccountDto>;
  private subs = new SubSink();
  formGroup: FormGroup;
  private usernameIsValid = false;
  roles: IRole[] = roles.filter((r) => r.isHealthDepartmentUser);
  loading = false;
  public confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  public passwordIncludesUsernameMatcher = new PasswordIncludesUsernameMatcher();
  isNew = false;
  private username = '';

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private entityService: AccountEntityService,
    private snackbarService: SnackbarService,
    private router: Router,
    private badRequestService: BadRequestService,
    public validationErrorService: ValidationErrorService
  ) {}

  ngOnInit() {
    this.account$ = this.route.parent.paramMap.pipe(
      switchMap((params) => this.entityService.loadOneFromStore(params.get('id'))),
      shareReplay(1),
      tap((data) => this.buildForm(data))
    );
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return this.formGroup?.pristine;
  }

  buildForm(account: AccountDto) {
    this.username = account.username;
    this.isNew = !account.accountId;
    this.usernameIsValid = !!account.accountId;

    this.formGroup = this.formBuilder.group(
      {
        firstName: new FormControl(account.firstName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        lastName: new FormControl(account.lastName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        password: new FormControl({ value: null, disabled: !this.isNew }, [PasswordValidator.secure]),
        passwordConfirm: new FormControl({ value: null, disabled: !this.isNew }, [Validators.required]),
        username: new FormControl(account.username, [
          Validators.required,
          () => (this.usernameIsValid ? null : { usernameInvalid: true }),
        ]),
        email: new FormControl(account.email, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
        ]),
        roles: new FormControl(account.roles, [ArrayValidator.minLengthArray(1)]),
      },
      {
        validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername],
      }
    );
    this.formGroup.controls.username.valueChanges
      .pipe(distinctUntilChanged(), debounceTime(1000))
      .subscribe((value) => this.changeUsername(value));
  }

  public changeUsername(username: string) {
    if (this.isNew || username !== this.username) {
      this.subs.add(
        this.authService
          .checkUsername(username)
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
          )
      );
    }
  }

  public submitForm(account: AccountDto, closeAfterSave: boolean) {
    if (this.formGroup.invalid) {
      this.snackbarService.warning('Bitte alle Pflichtfelder ausfüllen.');
      return;
    }
    this.loading = true;
    const accountToEdit = cloneDeep(account);
    Object.assign(accountToEdit, this.formGroup.value);

    if (this.isNew) {
      this.createAccount(accountToEdit, closeAfterSave);
    } else {
      this.editAccount(accountToEdit, closeAfterSave);
    }
  }

  private createAccount(account: AccountDto, closeAfterSave: boolean) {
    this.entityService
      .add(account)
      .subscribe(
        (result) => {
          this.formGroup.markAsPristine();
          this.snackbarService.success(
            `Der Account ${account.firstName} ${account.lastName} wurde erfolgreich angelegt`
          );
          if (closeAfterSave) {
            this.router.navigate(['/administration/accounts/account-list']);
          } else {
            this.router.navigate(['/administration/accounts/account-detail', result.accountId]);
          }
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, this.formGroup);
        }
      )
      .add(() => (this.loading = false));
  }

  private editAccount(account: AccountDto, closeAfterSave: boolean) {
    this.entityService
      .update(account)
      .subscribe(
        (result) => {
          this.formGroup.markAsPristine();
          this.snackbarService.success(
            `Die Accountdaten für ${account.firstName} ` + `${account.lastName} wurden erfolgreich aktualisiert`
          );
          if (closeAfterSave) {
            this.router.navigate(['/administration/accounts/account-list']);
          } else {
            this.router.navigate(['/administration/accounts/account-detail', result.accountId]);
          }
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, this.formGroup);
        }
      )
      .add(() => (this.loading = false));
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }
}
