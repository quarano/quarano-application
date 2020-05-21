import { ContactCaseActionListResolver } from './resolvers/contact-case-action-list.resolver';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactCaseCaseListResolver } from './resolvers/contact-case-case-list.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [ContactCaseActionListResolver, ContactCaseCaseListResolver]
})
export class HealthDepartmentContactCasesDomainModule { }
