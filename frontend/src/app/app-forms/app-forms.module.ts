import { PersonalDataFormComponent } from './personal-data-form/personal-data-form.component';
import { AngularMaterialModule } from './../angular-material/angular-material.module';
import { AlertModule } from './../ui/alert/alert.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './contact-person-form/contact-person-form.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    AlertModule,
    AngularMaterialModule
  ],
  declarations: [ContactPersonFormComponent, PersonalDataFormComponent],
  exports: [ContactPersonFormComponent, PersonalDataFormComponent]
})
export class AppFormsModule { }
