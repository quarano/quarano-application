import { ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MultipleAutocompleteComponent } from './multiple-autocomplete/multiple-autocomplete.component';
import { MultipleContactAutocompleteComponent } from './multiple-contact-autocomplete/multiple-contact-autocomplete.component';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule, ReactiveFormsModule],
  declarations: [MultipleAutocompleteComponent, MultipleContactAutocompleteComponent],
  exports: [MultipleAutocompleteComponent, MultipleContactAutocompleteComponent],
})
export class SharedUiMultipleAutocompleteModule {}
