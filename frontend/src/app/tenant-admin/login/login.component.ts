import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TenantService} from '../../services/tenant.service';
import {Tenant} from '../../models/tenant';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginFormGroup = new FormGroup({
    username: new FormControl(null, [Validators.minLength(1), Validators.maxLength(100)]),
    password: new FormControl(null, [Validators.minLength(1), Validators.maxLength(100)])
  });

  public loginError = false;

  constructor(private tenantService: TenantService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  public login() {
    this.tenantService.checkLogin(this.loginFormGroup.controls.username.value, this.loginFormGroup.controls.password.value)
      .subscribe(
        (tenant: Tenant) => {
          this.router.navigate(['/tenant-admin/clients']);
        },
        error => {
          this.loginError = true;
        }
      );
  }

}
