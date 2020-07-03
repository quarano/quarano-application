import { HealthDepartmentUiActionAlertModule } from '@qro/health-department/ui-action-alert';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
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
        resolve: { cases: IndexCaseCaseListResolver },
      },
    ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    HealthDepartmentUiActionAlertModule,
    HealthDepartmentDomainModule,
    RouterModule.forChild(routes),
  ],
  declarations: [ActionListComponent, CaseListComponent, IndexCasesComponent],
})
export class HealthDepartmentFeatureIndexCasesModule {}
