import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { PersonalDataFormComponent } from './personal-data-form/personal-data-form.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiAlertModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
  ],
  declarations: [PersonalDataFormComponent],
  exports: [PersonalDataFormComponent],
})
export class AppFormsModule {}
