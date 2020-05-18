import { AppFormsModule } from '../app-forms/app-forms.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { ProfileRoutingModule } from './profile-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile.component';

@NgModule({
  imports: [
    CommonModule,
    ProfileRoutingModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    AppFormsModule
  ],
  declarations: [ProfileComponent],
})
export class ProfileModule { }
