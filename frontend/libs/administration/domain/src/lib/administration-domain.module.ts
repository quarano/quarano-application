import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountDetailResolver } from './resolvers/account-detail.resolver';
import { AccountListResolver } from './resolvers/account-list.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [AccountListResolver, AccountDetailResolver],
})
export class AdministrationDomainModule {}
