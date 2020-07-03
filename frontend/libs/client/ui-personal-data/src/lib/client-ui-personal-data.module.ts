import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PersonalDataFormComponent } from './personal-data-form/personal-data-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [PersonalDataFormComponent],
  exports: [PersonalDataFormComponent],
  imports: [CommonModule, SharedUiMaterialModule, FormsModule, ReactiveFormsModule],
})
export class ClientUiPersonalDataModule {}
