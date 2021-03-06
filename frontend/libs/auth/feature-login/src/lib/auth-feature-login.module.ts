import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RouterModule } from '@angular/router';
import { SharedUtilTranslationModule } from '@qro/shared/util-translation';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUtilTranslationModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: LoginComponent,
      },
    ]),
  ],
  declarations: [LoginComponent],
})
export class AuthFeatureLoginModule {}
