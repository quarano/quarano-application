import { roles, IRole } from '@models/role';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { UserListItemDto } from '@models/user';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiService } from '@services/api.service';
import { SnackbarService } from '@services/snackbar.service';
import { Observable } from 'rxjs';
import { PasswordValidator } from '@validators/password-validator';
import { tap, finalize, distinctUntilChanged, debounceTime } from 'rxjs/operators';
import { ArrayValidator } from '@validators/array-validator';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.scss']
})
export class UserEditComponent implements OnInit, OnDestroy {
  user: UserListItemDto;
  private subs = new SubSink();
  formGroup: FormGroup;
  private usernameIsValid = false;
  roles: IRole[] = roles.filter(r => r.isHealthDepartmentUser);

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.user = data.user;
    }));
    this.buildForm();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return this.formGroup.pristine;
  }

  buildForm() {
    this.formGroup = this.formBuilder.group(
      {
        firstName: new FormControl(
          { value: this.user.firstName, disabled: !this.isNew },
          [Validators.required]),
        lastName: new FormControl(
          { value: this.user.lastName, disabled: !this.isNew },
          [Validators.required]),
        password: new FormControl({ value: null, disabled: !this.isNew }, [
          PasswordValidator.secure
        ]),
        passwordConfirm: new FormControl({ value: null, disabled: !this.isNew }, [
          Validators.required
        ]),
        username: new FormControl({ value: this.user.username, disabled: !this.isNew }, [
          Validators.required,
          Validators.email,
          () => this.usernameIsValid || this.isNew ? null : { usernameInvalid: true }
        ]),
        roles: new FormControl(this.user.roles, [ArrayValidator.minLengthArray(1)])
      }
    );
    this.formGroup.controls.username.valueChanges
      .pipe(distinctUntilChanged(), debounceTime(1000))
      .subscribe(value => this.changeUsername(value));
  }

  get isNew(): boolean {
    return this.user?.id == null;
  }

  public changeUsername(username: string) {
    this.apiService.checkUsername(username)
      .pipe(
        tap(() => {
          this.formGroup.get('username').disable();
        }),
        finalize(() => {
          this.formGroup.get('username').enable();
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
    if (this.formGroup.invalid) {
      this.snackbarService.warning('Bitte alle Pflichtfelder ausfüllen.');
      return;
    }
    Object.assign(this.user, this.formGroup.value);

    if (this.isNew) {
      this.createUser();
    } else {
      this.editUser();
    }
  }

  private createUser() {
    this.apiService.createHealthDepartmentUser(this.user)
      .subscribe(
        () => {
          this.snackbarService.success(`Der User ${this.user.firstName} ${this.user.lastName} wurde erfolgreich angelegt`);
          this.router.navigate(['/administration/users']);
        }
      );
  }

  private editUser() {
    this.apiService.editHealthDepartmentUser(this.user)
      .subscribe(
        () => {
          this.snackbarService.success(`Die Userdaten für ${this.user.firstName} ${this.user.lastName} wurden erfolgreich aktualisiert`);
          this.router.navigate(['/administration/users']);
        }
      );
  }
}
