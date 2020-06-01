import { ProfileComponent } from './../../../../../apps/quarano-frontend/src/app/modules/profile/profile.component';
import { PersonalDataFormComponent } from './../../../../../apps/quarano-frontend/src/app/modules/app-forms/personal-data-form/personal-data-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: '',
    component: ProfileComponent,
    resolve: {
      clientData: MyClientDataResolver,
    },
  },
];

@NgModule({
  declarations: [PersonalDataFormComponent, ProfileComponent],
  imports: [CommonModule, SharedUiMaterialModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(routes)],
})
export class ClientProfileModule {}
