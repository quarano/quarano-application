import { CheckboxFilterComponent } from './checkbox-filter.component';
import { AgGridModule } from 'ag-grid-angular';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { EmailButtonComponent } from './e-mail-button.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule, AgGridModule.withComponents([EmailButtonComponent])],
  declarations: [EmailButtonComponent, CheckboxFilterComponent],
  exports: [EmailButtonComponent, AgGridModule, CheckboxFilterComponent],
})
export class SharedUiAgGridModule {}
