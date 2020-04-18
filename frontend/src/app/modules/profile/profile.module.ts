import { AppFormsModule } from './../app-forms/app-forms.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { ProfileRoutingModule } from './profile-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile.component';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { MAT_MOMENT_DATE_FORMATS } from '@angular/material-moment-adapter';

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
  providers: [{ provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS }
  ]
})
export class ProfileModule { }
