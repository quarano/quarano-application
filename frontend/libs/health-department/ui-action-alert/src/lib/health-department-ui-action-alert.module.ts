import { FormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ActionAlertFilterComponent } from './action-alert/action-alert-filter.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionAlertComponent } from './action-alert/action-alert.component';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule, FormsModule],
  declarations: [ActionAlertComponent, ActionAlertFilterComponent],
  exports: [ActionAlertComponent, ActionAlertFilterComponent],
})
export class HealthDepartmentUiActionAlertModule {}
