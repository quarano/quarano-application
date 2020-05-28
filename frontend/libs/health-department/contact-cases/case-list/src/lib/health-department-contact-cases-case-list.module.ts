import { SharedUtilModule } from '@qro/shared/util';
import { ContactCaseCaseListResolver, HealthDepartmentContactCasesDomainModule } from '@qro/health-department/contact-cases/domain';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseListComponent } from './case-list/case-list.component';

@NgModule({
  imports: [CommonModule,
    SharedUiMaterialModule,
    SharedUtilModule,
    NgxDatatableModule,
    HealthDepartmentContactCasesDomainModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: CaseListComponent,
      resolve: { cases: ContactCaseCaseListResolver }
    }])],
  declarations: [CaseListComponent],
})
export class HealthDepartmentContactCasesCaseListModule { }
