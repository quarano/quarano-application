import { SharedUtilModule } from '@quarano-frontend/shared/util';
import { HealthDepartmentUiActionAlertModule } from '@quarano-frontend/health-department/ui-action-alert';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionListComponent } from './action-list/action-list.component';
import { ContactCaseActionListResolver, HealthDepartmentContactCasesDomainModule } from '@quarano-frontend/health-department/contact-cases/domain';

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
