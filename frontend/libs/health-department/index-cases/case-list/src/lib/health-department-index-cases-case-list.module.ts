import { IndexCaseCaseListResolver, HealthDepartmentIndexCasesDomainModule } from '@quarano-frontend/health-department/index-cases/domain';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseListComponent } from './case-list/case-list.component';

@NgModule({
  imports: [CommonModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    HealthDepartmentIndexCasesDomainModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: CaseListComponent,
      resolve: { cases: IndexCaseCaseListResolver }
    }])],
  declarations: [CaseListComponent],
})
export class HealthDepartmentIndexCasesCaseListModule { }
