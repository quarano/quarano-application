import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportCaseActionsResolver } from './resolvers/report-case-actions.resolver';
import { ReportCaseResolver } from './resolvers/report-case.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [ReportCaseActionsResolver, ReportCaseResolver],
})
export class HealthDepartmentDomainModule {}
