import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule
  ],
  declarations: [ConfirmationDialogComponent],
  exports: [ConfirmationDialogComponent],
  entryComponents: [ConfirmationDialogComponent]
})
export class ConfirmationDialogModule { }
