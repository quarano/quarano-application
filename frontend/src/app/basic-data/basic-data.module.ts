import { AppFormsModule } from './../app-forms/app-forms.module';
import { MyClientDataResolver } from './../resolvers/my-client-data.resolver';
import { MyFirstQueryResolver } from './../resolvers/my-first-query.resolver';
import { ContactPersonsResolver } from './../resolvers/contact-persons.resolver';
import { MultipleAutocompleteModule } from './../ui/multiple-autocomplete/multiple-autocomplete.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './basic-data.component';
import { BasicDataRoutingModule } from './basic-data-routing.module';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { MAT_MOMENT_DATE_FORMATS } from '@angular/material-moment-adapter';

@NgModule({
  imports: [
    CommonModule,
    BasicDataRoutingModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    MultipleAutocompleteModule,
    AppFormsModule
  ],
  declarations: [BasicDataComponent],
  providers:
    [
      ContactPersonsResolver,
      MyFirstQueryResolver,
      MyClientDataResolver,
      { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS }
    ]
})
export class BasicDataModule { }
