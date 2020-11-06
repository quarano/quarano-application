import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ExportComponent } from './export/export.component';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUtilFormsModule } from '@qro/shared/util-forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  {
    path: '',
    component: ExportComponent,
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    SharedUiMaterialModule,
    SharedUtilFormsModule,
    SharedUiButtonModule,
  ],
  declarations: [ExportComponent],
})
export class HealthDepartmentFeatureExportModule {}
