import { UserAdministrationModule } from './user-administration/user-administration.module';
import { IsAdminGuard } from '@guards/is-admin.guard';
import { AdministrationRoutingModule } from './administration-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    UserAdministrationModule
  ],
  providers: [IsAdminGuard]
})
export class AdministrationModule { }
