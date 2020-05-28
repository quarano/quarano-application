import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule],
  declarations: [ConfirmationDialogComponent],
  exports: [ConfirmationDialogComponent],
  entryComponents: [ConfirmationDialogComponent]
})
export class SharedUiConfirmationDialogModule { }
