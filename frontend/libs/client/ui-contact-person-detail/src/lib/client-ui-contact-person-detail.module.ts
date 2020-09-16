import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './/contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './/contact-person-dialog/contact-person-dialog.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MultipleContactAutocompleteComponent } from './/multiple-contact-autocomplete/multiple-contact-autocomplete.component';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { ContactLocationFormComponent } from './contact-location-form/contact-location-form.component';
import { MultipleLocationChipComponent } from './multiple-location-chip/multiple-location-chip.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiAlertModule,
    SharedUiButtonModule,
    SharedUiMultipleAutocompleteModule,
    SharedUtilTranslationModule,
    MatDialogModule,
  ],
  declarations: [
    ContactPersonDialogComponent,
    ContactPersonFormComponent,
    MultipleContactAutocompleteComponent,
    ContactLocationFormComponent,
    MultipleLocationChipComponent,
  ],
  exports: [
    ContactPersonDialogComponent,
    ContactPersonFormComponent,
    MultipleContactAutocompleteComponent,
    ContactLocationFormComponent,
    MultipleLocationChipComponent,
  ],
  entryComponents: [ContactPersonDialogComponent, ContactLocationFormComponent],
})
export class ClientUiContactPersonDetailModule {}
