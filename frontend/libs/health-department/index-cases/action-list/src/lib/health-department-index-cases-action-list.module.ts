import { HealthDepartmentUiActionAlertModule } from '@quarano-frontend/health-department/ui-action-alert';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionListComponent } from './action-list/action-list.component';
import { IndexCaseActionListResolver, HealthDepartmentIndexCasesDomainModule } from '@quarano-frontend/health-department/index-cases/domain';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    HealthDepartmentUiActionAlertModule,
    HealthDepartmentIndexCasesDomainModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: ActionListComponent,
      resolve: { actions: IndexCaseActionListResolver }
    }])],
  declarations: [ActionListComponent],
})
export class HealthDepartmentIndexCasesActionListModule { }
