import { AccountAdministrationModule } from './account-administration/account-administration.module';
import { IsAdminGuard } from '@guards/is-admin.guard';
import { AdministrationRoutingModule } from './administration-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    AccountAdministrationModule,
  ],
  providers: [IsAdminGuard]
})
export class AdministrationModule { }
