import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IndexCaseActionListResolver } from './resolvers/index-case-action-list.resolver';
import { IndexCaseCaseListResolver } from './resolvers/index-case-case-list.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [IndexCaseActionListResolver, IndexCaseCaseListResolver]
})
export class HealthDepartmentIndexCasesDomainModule { }
