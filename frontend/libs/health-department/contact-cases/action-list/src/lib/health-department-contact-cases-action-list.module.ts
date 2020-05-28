import { SharedUtilModule } from '@qro/shared/util';
import { HealthDepartmentUiActionAlertModule } from '@qro/health-department/ui-action-alert';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionListComponent } from './action-list/action-list.component';
import { ContactCaseActionListResolver, HealthDepartmentContactCasesDomainModule } from '@qro/health-department/contact-cases/domain';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    HealthDepartmentUiActionAlertModule,
    HealthDepartmentContactCasesDomainModule,
    SharedUtilModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: ActionListComponent,
      resolve: { actions: ContactCaseActionListResolver }
    }])],
  declarations: [ActionListComponent]
})
export class HealthDepartmentContactCasesActionListModule { }
