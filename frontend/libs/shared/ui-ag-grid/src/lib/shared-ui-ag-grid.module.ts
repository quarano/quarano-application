import { FormsModule } from '@angular/forms';
import { CheckboxFilterComponent } from './checkbox-filter.component';
import { UnorderedListComponent } from './unordered-list.component';
import { AgGridModule } from 'ag-grid-angular';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { EmailButtonComponent } from './e-mail-button.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    AgGridModule.withComponents([EmailButtonComponent, UnorderedListComponent, CheckboxFilterComponent]),
  ],
  declarations: [EmailButtonComponent, UnorderedListComponent, CheckboxFilterComponent],
  exports: [EmailButtonComponent, AgGridModule, UnorderedListComponent, CheckboxFilterComponent],
})
export class SharedUiAgGridModule {}
