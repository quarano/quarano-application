import { Store } from '@ngrx/store';
import { ValidationErrorGenerator, PasswordValidator, ConfirmValidPasswordMatcher } from '@qro/shared/util-forms';
import { BadRequestService } from '@qro/shared/ui-error';
import { AuthService, UserService, AuthStore } from '@qro/auth/domain';
import { MatInput } from '@angular/material/input';
import { SubSink } from 'subsink';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Component, OnInit, OnDestroy, Injector } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { tap, switchMap } from 'rxjs/operators';

@Component({
  selector: 'qro-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent implements OnInit, OnDestroy {
  loading = false;
  formGroup: FormGroup;
  confirmValidParentMatcher = new ConfirmValidPasswordMatcher();
  private subs = new SubSink();
  errorGenerator = ValidationErrorGenerator;
  /**
   * Need to inject the Dialog stuff this way,
   * otherwise a component can't be used as a dialog and as usual component at the same time
   */
  private dialogRef = null;
  private dialogData;

  constructor(
    private userService: UserService,
    private snackbarService: TranslatedSnackbarService,
    private authStore: AuthStore,
    private router: Router,
    private authService: AuthService,
    private badRequestService: BadRequestService,
    private injector: Injector,
    private store: Store
  ) {
    this.dialogRef = this.injector.get(MatDialogRef, null);
    this.dialogData = this.injector.get(MAT_DIALOG_DATA, null);
  }

  ngOnInit() {
    this.createForm();
    this.subs.add(this.authStore.user$.subscribe((user) => this.formGroup.controls.username.setValue(user.username)));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  createForm() {
    this.formGroup = new FormGroup(
      {
        username: new FormControl({ value: '', disabled: true }),
        current: new FormControl(null, [Validators.required]),
        password: new FormControl(null, [Validators.required, PasswordValidator.secure]),
        passwordConfirm: new FormControl(null, [Validators.required]),
      },
      {
        validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername],
      }
    );
  }

  submitForm() {
    if (this.formGroup.valid) {
      this.loading = true;
      this.subs.add(
        this.authService
          .changePassword(this.formGroup.value)
          .pipe(switchMap(() => this.snackbarService.success('CHANGE_PASSWORD.PASSWORT_WURDE_GEÄNDERT')))
          .subscribe(
            () => {
              if (this.dialogData?.mode === 'initialPasswordChange') {
                this.dialogRef.close('success');
              } else {
                this.router.navigate(['/general/welcome']);
              }
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, this.formGroup);
            }
          )
          .add(() => (this.loading = false))
      );
    }
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
