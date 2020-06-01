import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForgottenContactBannerComponent } from './components/forgotten-contact-banner/forgotten-contact-banner.component';
import { ForgottenContactDialogComponent } from './components/forgotten-contact-dialog/forgotten-contact-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiMultipleAutocompleteModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
  ],
  declarations: [ForgottenContactBannerComponent, ForgottenContactDialogComponent],
  entryComponents: [ForgottenContactBannerComponent, ForgottenContactDialogComponent],
  exports: [ForgottenContactBannerComponent, ForgottenContactDialogComponent],
})
export class ClientDiaryForgottenContactModule {}
