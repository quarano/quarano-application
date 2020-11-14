import { ApiService } from '@qro/shared/util-data-access';
import {
  ConfirmValidPasswordMatcher,
  DeactivatableComponent,
  PasswordIncludesUsernameMatcher,
  PasswordValidator,
  ValidationErrorService,
} from '@qro/shared/util-forms';
import { Component, HostListener, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountDto, AccountEntityService } from '@qro/administration/domain';
import { SubSink } from 'subsink';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { BadRequestService } from '@qro/shared/ui-error';
import { shareReplay, switchMap, tap } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';

@Component({
  selector: 'qro-account-reset-password',
  templateUrl: './account-reset-password.component.html',
  styleUrls: ['./account-reset-password.component.scss'],
})
export class AccountResetPasswordComponent implements OnInit, OnDestroy, DeactivatableComponent {
  account$: Observable<AccountDto>;
  private subs = new SubSink();
  formGroup: FormGroup;
  loading = false;
  public confirmValidPasswordMatcher = new ConfirmValidPasswordMatcher();
  public passwordIncludesUsernameMatcher = new PasswordIncludesUsernameMatcher();

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private apiService: ApiService,
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
      tap((account) => this.buildForm(account))
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
    this.formGroup = this.formBuilder.group(
      {
        username: new FormControl(account.username),
        password: new FormControl(null, PasswordValidator.secure),
        passwordConfirm: new FormControl(null, Validators.required),
      },
      {
        validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername],
      }
    );
  }

  public submitForm(account: AccountDto, closeAfterSave: boolean) {
    if (this.formGroup.invalid) {
      this.snackbarService.warning('Bitte alle Pflichtfelder ausfüllen.');
      return;
    }
    this.loading = true;
    const { username, ...remaining } = this.formGroup.value;

    this.subs.add(
      this.apiService
        .putApiCall(account, 'reset-password', remaining)
        .subscribe(
          (result) => {
            this.formGroup.markAsPristine();
            this.snackbarService.success(
              `Das Passwort für Account ${account.firstName} ${account.lastName} wurde erfolgreich aktualisiert`
            );
            if (closeAfterSave) {
              this.router.navigate(['/administration/accounts/account-list']);
            } else {
              this.router.navigate(['/administration/accounts/account-detail', account.accountId]);
            }
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }
}
