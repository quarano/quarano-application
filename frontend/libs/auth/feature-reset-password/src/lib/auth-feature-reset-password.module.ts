import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { PasswordForgottenComponent } from './password-forgotten/password-forgotten.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedUtilTranslationModule } from '@qro/shared/util-translation';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    SharedUtilTranslationModule,
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
