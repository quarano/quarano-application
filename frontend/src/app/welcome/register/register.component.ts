import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {PasswordValidator} from '../../validators/password-validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  public registrationForm = new FormGroup({
    clientKey: new FormControl(null, [
      Validators.required,
      Validators.minLength(9),
      Validators.maxLength(30)
    ]),
    username: new FormControl(null, [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(30)
    ]),
    password: new FormControl(null, [
      Validators.required,
      PasswordValidator.secure
    ]),
    passwordConfirm: new FormControl(null, [
      Validators.required
    ])
  }, {
    validators: [PasswordValidator.mustMatch, PasswordValidator.mustNotIncludeUsername]
  });

  constructor() {
  }

  ngOnInit(): void {
  }

}
