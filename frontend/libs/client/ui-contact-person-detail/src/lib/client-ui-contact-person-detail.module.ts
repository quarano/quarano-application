import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './/contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './/contact-person-dialog/contact-person-dialog.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MultipleContactAutocompleteComponent } from './/multiple-contact-autocomplete/multiple-contact-autocomplete.component';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    SharedUiMultipleAutocompleteModule,
    SharedUtilTranslationModule,
  ],
  declarations: [ContactPersonDialogComponent, ContactPersonFormComponent, MultipleContactAutocompleteComponent],
  exports: [ContactPersonDialogComponent, ContactPersonFormComponent, MultipleContactAutocompleteComponent],
  entryComponents: [ContactPersonDialogComponent],
})
export class ClientUiContactPersonDetailModule {}
