import { LocationFormComponent } from './location-form/location-form.component';
import { LocationDialogComponent } from './location-dialog/location-dialog.component';
import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
  declarations: [LocationDialogComponent, LocationFormComponent],
  exports: [LocationDialogComponent, LocationFormComponent],
})
export class ClientUiLocationDetailModule {}
