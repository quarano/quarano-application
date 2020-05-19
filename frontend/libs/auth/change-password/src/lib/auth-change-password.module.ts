import { AngularMaterialModule } from './../../../../../apps/quarano-frontend/src/app/modules/angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: ChangePasswordComponent,
    }])
  ],
  declarations: [ChangePasswordComponent],
})
export class AuthChangePasswordModule { }
