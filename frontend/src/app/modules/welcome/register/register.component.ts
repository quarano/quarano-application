import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PasswordValidator } from '@validators/password-validator';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { filter, finalize, map, take, tap } from 'rxjs/operators';
import { ApiService } from '@services/api.service';
import { Register } from '@models/register';
import { SnackbarService } from '@services/snackbar.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  private codeIsValid = false;
  private usernameIsValid = false;

  public registrationForm = new FormGroup({
    clientCode: new FormControl(null, [
      Validators.required,
      () => this.codeIsValid ? null : { codeInvalid: true }
    ]),
    username: new FormControl(null, [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(30),
      () => this.usernameIsValid ? null : { usernameInvalid: true }
    ]),
    password: new FormControl(null, [
      PasswordValidator.secure
    ]),
    passwordConfirm: new FormControl(null, [
      Validators.required
    ]),
    email: new FormControl(null, [
      Validators.required,
      Validators.email
    ]),
    dateOfBirth: new FormControl(null, [
      Validators.required
    ]),
    dataProtectionConfirmed: new FormControl(null, [Validators.required])
  }, {
    validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername]
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.checkUrlCode();
  }

  private checkUrlCode() {
    this.route.paramMap.pipe(
      take(1),
      map((params: ParamMap) => params.get('clientcode')),
      filter(code => code != null),
      /*tap(code => urlParamCode = code),
      switchMap(code => this.apiService.checkClientCode(code)),
      tap(response => {
        if (!response) {
          this.snackbarService.error(
            'Der verwendete Code ist leider ungültig. Bitte wenden Sie sich zur Klärung an Ihr Gesundheitsamt');
          this.router.navigate(['/welcome/login']);
        }
      })*/
    ).subscribe(
      (code) => {
        if (code) {
          this.codeIsValid = true;
          this.registrationForm.get('clientCode').setValue(code);
          this.registrationForm.get('clientCode').updateValueAndValidity();
        }
      },
      () => {
        console.log('The code provided as URL parameter is invalid.');
      }
    );
  }

  public changeCode(code: string | null) {
    const clientCode = code != null ? code : this.registrationForm.controls.clientCode.value;
    this.apiService.checkClientCode(clientCode)
      .pipe(
        tap(() => {
          this.registrationForm.get('clientCode').disable();
        })
      )
      .subscribe(
        () => {
          this.codeIsValid = true;
        },
        () => {
          this.codeIsValid = false;
        },
        () => {
          this.registrationForm.get('clientCode').enable();
        }
      );
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
        () => {
          this.usernameIsValid = true;
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

    const register: Register = Object.assign(this.registrationForm.value);

    this.apiService.registerClient(register)
      .subscribe(
        () => {
          this.snackbarService.success(`Die Registrierung war erfolgreich. Bitte loggen Sie sich ein.`);
          this.router.navigate(['/welcome/login']);
        },
        () => {
          this.snackbarService.error('Es ist ein Fehler aufgetreten. Bitte später erneut versuchen.');
        }
      );
  }
}
