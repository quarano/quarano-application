import { MyClientDataResolver } from '@qro/client/domain';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PersonalDataFormComponent } from './components/personal-data-form/personal-data-form.component';
import { ProfileComponent } from './components/profile/profile.component';

const routes: Routes = [
  {
    path: '',
    component: ProfileComponent,
    pathMatch: 'full',
    resolve: {
      clientData: MyClientDataResolver,
    },
  },
];

@NgModule({
  declarations: [PersonalDataFormComponent, ProfileComponent],
  imports: [CommonModule, SharedUiMaterialModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(routes)],
  exports: [PersonalDataFormComponent],
})
export class ClientProfileModule {}
