import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IndexCasesComponent } from './index-cases/index-cases.component';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    RouterModule.forChild([{
      path: '',
      component: IndexCasesComponent,
      children: [
        {
          path: '',
          redirectTo: 'case-list',
          pathMatch: 'full',
        },
        {
          path: 'action-list', loadChildren: () =>
            import('@quarano-frontend/health-department/index-cases/action-list')
              .then(m => m.HealthDepartmentIndexCasesActionListModule)
        },
        {
          path: 'case-list', loadChildren: () =>
            import('@quarano-frontend/health-department/index-cases/case-list')
              .then(m => m.HealthDepartmentIndexCasesCaseListModule)
        }
      ]
    }
    ])],
  declarations: [IndexCasesComponent],
})
export class HealthDepartmentIndexCasesShellModule { }
