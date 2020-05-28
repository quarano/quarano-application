import { AppFormsModule } from '../app-forms/app-forms.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProfileRoutingModule } from './profile-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    ProfileRoutingModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    AppFormsModule
  ],
  declarations: [ProfileComponent],
})
export class ProfileModule { }
