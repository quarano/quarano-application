import { ContactPersonsResolver } from './../resolvers/contact-persons.resolver';
import { ContactModule } from './../contact/contact.module';
import { MultipleAutocompleteModule } from './../ui/multiple-autocomplete/multiple-autocomplete.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './basic-data.component';
import { BasicDataRoutingModule } from './basic-data-routing.module';

@NgModule({
  imports: [
    CommonModule,
    BasicDataRoutingModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    MultipleAutocompleteModule,
    ContactModule
  ],
  declarations: [BasicDataComponent],
  providers: [ContactPersonsResolver]
})
export class BasicDataModule { }
