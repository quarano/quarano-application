import { HttpResponse } from '@angular/common/http';
import { ValidationErrorGenerator } from '@qro/shared/util-forms';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { filter, take } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { UserService } from '@qro/auth/domain';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ChangePasswordComponent } from '@qro/auth/feature-change-password';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'qro-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loading = false;
  errorGenerator = ValidationErrorGenerator;

  public loginFormGroup = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required),
  });

  constructor(
    private userService: UserService,
    private snackbarService: SnackbarService,
    private router: Router,
    private matDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.userService.isLoggedIn$
      .pipe(
        take(1),
        filter((loggedin) => loggedin)
      )
      .subscribe(() => {
        this.userService.logout();
      });
  }

  public submitForm() {
    this.loading = true;
    this.userService
      .login(this.loginFormGroup.controls.username.value, this.loginFormGroup.controls.password.value)
      .subscribe(
        (resData) => {
          this.snackbarService.success('Willkommen bei quarano');

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
          if (error.error === 'Case already closed!') {
            this.snackbarService.message('Ihr Fall ist bereits geschlossen');
          } else {
            this.snackbarService.error('Benutzername oder Passwort falsch');
          }
        }
      )
      .add(() => (this.loading = false));
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
