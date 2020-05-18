import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from '../../modules/angular-material/angular-material.module';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    AngularMaterialModule
  ],
  declarations: [ConfirmationDialogComponent],
  exports: [ConfirmationDialogComponent],
  entryComponents: [ConfirmationDialogComponent]
})
export class ConfirmationDialogModule { }
