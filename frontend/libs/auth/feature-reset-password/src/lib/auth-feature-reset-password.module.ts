import { PasswordResetComponent } from './password-reset/password-reset.component';
import { PasswordForgottenComponent } from './password-forgotten/password-forgotten.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,

    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: PasswordForgottenComponent,
      },
      {
        path: ':token',
        component: PasswordResetComponent,
      },
    ]),
  ],
  declarations: [PasswordForgottenComponent, PasswordResetComponent],
})
export class AuthFeatureResetPasswordModule {}
