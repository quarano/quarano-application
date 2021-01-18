import { HealthDepartmentDomainModule } from '@qro/health-department/domain';
import { FormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ExportDialogComponent } from './export-dialog/export-dialog.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, FormsModule, SharedUiMaterialModule, SharedUiButtonModule, HealthDepartmentDomainModule],
  declarations: [ExportDialogComponent],
  exports: [ExportDialogComponent],
})
export class HealthDepartmentUiExportDialogModule {}
