import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PersonalDataFormComponent } from './components/personal-data-form/personal-data-form.component';

@NgModule({
  declarations: [PersonalDataFormComponent],
  exports: [PersonalDataFormComponent],
  imports: [CommonModule, SharedUiMaterialModule, FormsModule, ReactiveFormsModule],
})
export class ClientProfileUiPersonalDataModule {}
