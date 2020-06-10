import { ValidationErrorGenerator } from '@qro/shared/util-form-validation';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { filter, take } from 'rxjs/operators';
import { MatInput } from '@angular/material/input';
import { UserService } from '@qro/auth/domain';
import { SnackbarService } from '@qro/shared/util';

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

  constructor(private userService: UserService, private snackbarService: SnackbarService, private router: Router) {}

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
        (_) => {
          this.snackbarService.success('Willkommen bei quarano');
          if (this.userService.isHealthDepartmentUser) {
            this.router.navigate(['/health-department/index-cases/case-list']);
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

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }
}
