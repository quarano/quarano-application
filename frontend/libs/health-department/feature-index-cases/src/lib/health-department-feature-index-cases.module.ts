import { HealthDepartmentUiExportDialogModule } from '@qro/health-department/ui-export-dialog';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiAgGridModule } from '@qro/shared/ui-ag-grid';
import { FormsModule } from '@angular/forms';
import { HealthDepartmentUiActionAlertModule } from '@qro/health-department/ui-action-alert';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseListComponent } from './case-list/case-list.component';
import { ActionListComponent } from './action-list/action-list.component';
import { IndexCasesComponent } from './index-cases/index-cases.component';
import {
  IndexCaseActionListResolver,
  IndexCaseCaseListResolver,
  HealthDepartmentDomainModule,
} from '@qro/health-department/domain';

const routes: Routes = [
  {
    path: '',
    component: IndexCasesComponent,
    children: [
      {
        path: '',
        redirectTo: 'case-list',
        pathMatch: 'full',
      },
      {
        path: 'action-list',
        pathMatch: 'full',
        component: ActionListComponent,
        resolve: { actions: IndexCaseActionListResolver },
      },
      {
        path: 'case-list',
        pathMatch: 'full',
        component: CaseListComponent,
        resolve: { casesLoaded: IndexCaseCaseListResolver },
      },
    ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    HealthDepartmentUiActionAlertModule,
    HealthDepartmentDomainModule,
    RouterModule.forChild(routes),
    SharedUiAgGridModule,
    SharedUiButtonModule,
    FormsModule,
    HealthDepartmentUiExportDialogModule,
  ],
  declarations: [ActionListComponent, CaseListComponent, IndexCasesComponent],
})
export class HealthDepartmentFeatureIndexCasesModule {}
