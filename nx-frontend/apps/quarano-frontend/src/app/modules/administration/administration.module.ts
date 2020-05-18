import { AccountAdministrationModule } from './account-administration/account-administration.module';
import { AdministrationRoutingModule } from './administration-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {IsAdminGuard} from '../../guards/is-admin.guard';

@NgModule({
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    AccountAdministrationModule,
  ],
  providers: [IsAdminGuard]
})
export class AdministrationModule { }
