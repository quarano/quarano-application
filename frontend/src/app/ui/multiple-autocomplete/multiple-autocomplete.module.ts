import { ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './../../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MultipleAutocompleteComponent } from './multiple-autocomplete.component';

@NgModule({
  imports: [
    CommonModule,
    AngularMaterialModule,
    ReactiveFormsModule
  ],
  declarations: [MultipleAutocompleteComponent],
  exports: [MultipleAutocompleteComponent]
})
export class MultipleAutocompleteModule { }
