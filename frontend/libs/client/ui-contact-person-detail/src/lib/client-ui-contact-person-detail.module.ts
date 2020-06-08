import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './components/contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './components/contact-person-dialog/contact-person-dialog.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiAlertModule,
    SharedUiButtonModule,
  ],
  declarations: [ContactPersonDialogComponent, ContactPersonFormComponent],
  exports: [ContactPersonDialogComponent, ContactPersonFormComponent],
  entryComponents: [ContactPersonDialogComponent],
})
export class ClientUiContactPersonDetailModule {}
