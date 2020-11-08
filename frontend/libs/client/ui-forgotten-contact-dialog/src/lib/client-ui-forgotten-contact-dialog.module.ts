import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgottenContactDialogComponent } from './forgotten-contact-dialog/forgotten-contact-dialog.component';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { ClientUiContactPersonDetailModule } from '@qro/client/ui-contact-person-detail';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUtilTranslationModule,
    SharedUiMultipleAutocompleteModule,
    ClientUiContactPersonDetailModule,
  ],
  declarations: [ForgottenContactDialogComponent],
  exports: [ForgottenContactDialogComponent],
})
export class ClientUiForgottenContactDialogModule {}
