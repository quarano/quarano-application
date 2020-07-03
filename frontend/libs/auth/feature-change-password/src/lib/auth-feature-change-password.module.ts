import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChangePasswordComponent } from './/change-password/change-password.component';
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
})
export class AuthFeatureChangePasswordModule {}
