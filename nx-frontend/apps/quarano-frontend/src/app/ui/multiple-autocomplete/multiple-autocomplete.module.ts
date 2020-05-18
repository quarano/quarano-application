import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from '../../modules/angular-material/angular-material.module';
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
