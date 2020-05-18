import { AppFormsModule } from '../app-forms/app-forms.module';
import { RouterModule } from '@angular/router';
import { ContactPersonComponent } from './contact-person/contact-person.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactRoutingModule } from './contact-persons-routing.module';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ContactPersonsComponent } from './contact-persons.component';
import {AlertModule} from '../../ui/alert/alert.module';
import {ConfirmationDialogModule} from '../../ui/confirmation-dialog/confirmation-dialog.module';
import {ContactPersonsResolver} from '../../resolvers/contact-persons.resolver';
import {ContactPersonResolver} from '../../resolvers/contact-person.resolver';

const COMPONENTS = [
  ContactPersonsComponent,
  ContactPersonComponent,
];

@NgModule({
  imports: [
    CommonModule,
    ContactRoutingModule,
    AngularMaterialModule,
    ReactiveFormsModule,
    RouterModule,
    AlertModule,
    ConfirmationDialogModule,
    AppFormsModule
  ],
  declarations: [...COMPONENTS],
  providers: [ContactPersonsResolver, ContactPersonResolver],
})
export class ContactPersonsModule { }
