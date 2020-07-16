import { ChangePasswordComponent } from './change-password/change-password.component';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: ChangePasswordComponent,
      },
    ]),
  ],
  declarations: [ChangePasswordComponent],
  exports: [ChangePasswordComponent],
})
export class AuthFeatureChangePasswordModule {}
