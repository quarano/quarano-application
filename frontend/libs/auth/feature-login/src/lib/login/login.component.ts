import { SubSink } from 'subsink';
import { HttpResponse } from '@angular/common/http';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { filter, take, map, switchMap } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { UserService } from '@qro/auth/domain';
import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { ChangePasswordComponent } from '@qro/auth/feature-change-password';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'qro-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
  loading = false;
  private subs = new SubSink();

  public loginFormGroup = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required),
  });

  constructor(
    private userService: UserService,
    private translatedSnackbarService: TranslatedSnackbarService,
    private snackbarService: SnackbarService,
    private router: Router,
    private matDialog: MatDialog,
    public validationErrorService: ValidationErrorService
  ) {}

  ngOnInit(): void {
    this.subs.add(
      this.userService.isLoggedIn$
        .pipe(
          take(1),
          filter((loggedin) => loggedin)
        )
        .subscribe(() => {
          this.userService.logout();
        })
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  public submitForm() {
    this.loading = true;
    this.subs.add(
      this.userService
        .login(this.loginFormGroup.controls.username.value, this.loginFormGroup.controls.password.value)
        .pipe(
          switchMap((resData) =>
            this.translatedSnackbarService.success('LOGIN.WILLKOMMEN_BEI_QUARANO').pipe(map((res) => resData))
          )
        )
        .subscribe(
          (resData) => {
            if (this.userService.isHealthDepartmentUser) {
              if (this.checkIfPasswordChangeNeeded(resData)) {
                this.openPasswordChangeDialog();
              } else {
                this.router.navigate(['/health-department/index-cases/case-list']);
              }
            } else {
              this.router.navigate(['/client/diary/diary-list']);
            }
          },
          (error) => {
            if (error.error) {
              this.subs.add(this.translatedSnackbarService.error(error.error).subscribe());
            } else {
              this.subs.add(
                this.translatedSnackbarService.error('LOGIN.BENUTZERNAME_ODER_PASSWORT_FALSCH').subscribe()
              );
            }
          }
        )
        .add(() => (this.loading = false))
    );
  }

  /**
   * Checks whether in the _links object there is only one element and if so, if it is
   * referreing to the password page, then the user needs to change his
   * initial password
   * @param response the data received from the backend from the login api Call
   */
  checkIfPasswordChangeNeeded(response: HttpResponse<any>): boolean {
    const resBody = response.body;
    if (resBody && resBody._links && resBody._links.length > 0) {
      const changeNeeded = !!resBody._links.find((link) => link.rel === 'change-password');
      return changeNeeded;
    } else {
      return false;
    }
  }

  /**
   * Opens the Change Password Component with a Material Dialog and
   * routes the user afterwards if the password change was successful
   */
  openPasswordChangeDialog(): void {
    this.matDialog
      .open(ChangePasswordComponent, { disableClose: true, data: { mode: 'initialPasswordChange' } })
      .afterClosed()
      .subscribe((result) => {
        if (result === 'success') {
          this.router.navigate(['/health-department/index-cases/case-list']);
        }
      });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
