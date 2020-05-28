import { IndexCaseCaseListResolver, HealthDepartmentIndexCasesDomainModule } from '@qro/health-department/index-cases/domain';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
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
