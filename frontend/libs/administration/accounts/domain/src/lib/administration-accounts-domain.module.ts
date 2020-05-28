import { AccountDetailResolver } from './resolvers/account-detail.resolver';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountListResolver } from './resolvers/account-list.resolver';

@NgModule({
  imports: [CommonModule],
  providers: [AccountListResolver, AccountDetailResolver]
})
export class AdministrationAccountsDomainModule { }
