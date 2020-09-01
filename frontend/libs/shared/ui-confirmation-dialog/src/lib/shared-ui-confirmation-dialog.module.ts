import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { TranslatedConfirmationDialogComponent } from './translated-confirmation-dialog/translated-confirmation-dialog.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule, SharedUtilTranslationModule],
  declarations: [ConfirmationDialogComponent, TranslatedConfirmationDialogComponent],
  exports: [ConfirmationDialogComponent, TranslatedConfirmationDialogComponent],
  entryComponents: [ConfirmationDialogComponent, TranslatedConfirmationDialogComponent],
})
export class SharedUiConfirmationDialogModule {}
