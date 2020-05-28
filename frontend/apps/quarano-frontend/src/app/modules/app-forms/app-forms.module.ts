import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { PersonalDataFormComponent } from './personal-data-form/personal-data-form.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonFormComponent } from './contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './contact-person-dialog/contact-person-dialog.component';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiAlertModule,
    SharedUiMaterialModule,
    SharedUiButtonModule
  ],
  declarations: [ContactPersonFormComponent, PersonalDataFormComponent, ContactPersonDialogComponent],
  exports: [ContactPersonFormComponent, PersonalDataFormComponent, ContactPersonDialogComponent],
  entryComponents: [ContactPersonDialogComponent]
})
export class AppFormsModule { }
