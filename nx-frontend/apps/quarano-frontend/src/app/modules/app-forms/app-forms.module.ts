import { PersonalDataFormComponent } from './personal-data-form/personal-data-form.component';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './contact-person-dialog/contact-person-dialog.component';
import {AlertModule} from '../../ui/alert/alert.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    AlertModule,
    AngularMaterialModule
  ],
  declarations: [ContactPersonFormComponent, PersonalDataFormComponent, ContactPersonDialogComponent],
  exports: [ContactPersonFormComponent, PersonalDataFormComponent, ContactPersonDialogComponent],
  entryComponents: [ContactPersonDialogComponent]
})
export class AppFormsModule { }
