import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SnackbarService} from '../../services/snackbar.service';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public loginFormGroup = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required)
  });

  constructor(private userService: UserService,
              private snackbarService: SnackbarService,
              private router: Router) { }

  public submitForm() {
    this.userService.login(this.loginFormGroup.controls.username.value, this.loginFormGroup.controls.password.value)
      .subscribe(
        (response) => {
          this.snackbarService.success('Login erfolgreich');

          if (this.userService.hasRole('ROLE_HD_ADMIN') || this.userService.hasRole('ROLE_HD_CASE_AGENT')) {
            this.router.navigate(['/tenant-admin']);
          } else {
            this.router.navigate(['/diary']);
          }
        },
        () => {
          this.snackbarService.error('Benutzername, oder Passwort falsch');
        }
      );
  }

}
