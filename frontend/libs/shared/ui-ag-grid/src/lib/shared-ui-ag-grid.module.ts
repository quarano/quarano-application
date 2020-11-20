import { UnorderedListComponent } from './unordered-list.component';
import { CheckboxFilterComponent } from './checkbox-filter.component';
import { AgGridModule } from 'ag-grid-angular';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { EmailButtonComponent } from './e-mail-button.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    AgGridModule.withComponents([EmailButtonComponent, UnorderedListComponent]),
  ],
  declarations: [EmailButtonComponent, CheckboxFilterComponent, UnorderedListComponent],
  exports: [EmailButtonComponent, AgGridModule, CheckboxFilterComponent, UnorderedListComponent],
})
export class SharedUiAgGridModule {}
