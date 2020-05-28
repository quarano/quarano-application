import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MultipleAutocompleteComponent } from './multiple-autocomplete.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    ReactiveFormsModule
  ],
  declarations: [MultipleAutocompleteComponent],
  exports: [MultipleAutocompleteComponent]
})
export class MultipleAutocompleteModule { }
