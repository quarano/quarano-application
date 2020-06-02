import { IsHealthDepartmentUserDirective } from './directives/is-health-department-user.directive';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportCaseActionsResolver } from './resolvers/report-case-actions.resolver';
import { ReportCaseResolver } from './resolvers/report-case.resolver';

@NgModule({
  imports: [CommonModule],
  declarations: [IsHealthDepartmentUserDirective],
  exports: [IsHealthDepartmentUserDirective],
  providers: [ReportCaseActionsResolver, ReportCaseResolver],
})
export class HealthDepartmentDomainModule {}
